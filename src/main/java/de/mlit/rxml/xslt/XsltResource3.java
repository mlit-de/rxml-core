/*
 * Copyright (C) 2014-2021 Markus Lauer
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package de.mlit.rxml.xslt;


import de.mlit.rxml.api.ResourceFactory;
import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.SourceInfo;
import de.mlit.rxml.api.helper.AbstractResource;
import de.mlit.rxml.api.helper.ResourceAdapter;
import de.mlit.rxml.resolver.RxmlInputSource;
import de.mlit.rxml.resolver.RxmlResolver;
import de.mlit.rxml.resolver.RxmlSAXSource;
import net.sf.saxon.Configuration;
import net.sf.saxon.Controller;
import net.sf.saxon.event.Builder;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.ReceivingContentHandler;
import net.sf.saxon.jaxp.TemplatesImpl;
import net.sf.saxon.jaxp.TransformerImpl;
import net.sf.saxon.lib.FeatureKeys;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.s9api.*;
import net.sf.saxon.trans.XPathException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XsltResource3 extends AbstractResource implements SaxResource {


    protected SaxResource stylesheet;
    protected SaxResource document;

    protected Map<String, Object> map;
    protected RxmlResolver resolver = this.resolver;
    protected String mode;

    //protected static SAXTransformerFactory factory = (SAXTransformerFactory)TransformerFactory.newInstance();

    protected SAXTransformerFactory factory;
    protected Processor processor;


    public XsltResource3(SaxResource stylesheet, SaxResource document,
                         Map<String, Object> map, RxmlResolver resolver, List<AbstractSaxonExtension> extensions, String mode) {
        this.stylesheet = stylesheet;
        this.document = document;
        this.map = map;
        this.mode = mode;


        Configuration configuration = getConfiguration(extensions);
        if (resolver != null) {
            configuration.setURIResolver(resolver);
        }
        this.processor = new Processor(configuration);


    }


    protected Configuration getConfiguration(List<AbstractSaxonExtension> extensions) {
        Configuration configuration = Configuration.newConfiguration();
        configuration.setConfigurationProperty(FeatureKeys.XSLT_VERSION, "3.0");
        configuration.setConfigurationProperty(FeatureKeys.XQUERY_VERSION, "3.1");
        configuration.setConfigurationProperty(FeatureKeys.TRACE_EXTERNAL_FUNCTIONS, false);
        if (extensions != null) {
            for (AbstractSaxonExtension ext : extensions) {
                if (ext != null) {
                    ext.install(configuration);
                }
            }
        }
        return configuration;
    }


    @Override
    public void runOn(ContentHandler ch) throws SAXException, IOException {

        Templates templ = null;


        XsltCompiler xsltCompiler = processor.newXsltCompiler();
        final ErrorListener errorListener = xsltCompiler.getErrorListener();
        final List<String> errors = new ArrayList<String>();

        ErrorListener errorListener1 = new ErrorListener() {
            @Override
            public void warning(TransformerException exception) throws TransformerException {
                if(errorListener!=null)
                    errorListener.warning(exception);
            }

            @Override
            public void error(TransformerException exception) throws TransformerException {
                if(errorListener!=null)
                    errorListener.error(exception);
            }

            @Override
            public void fatalError(TransformerException exception) throws TransformerException {
                if (exception instanceof XPathException) {
                    errors.add(
                            ((XPathException) exception).getLocationAsString() + " " +
                                    ((XPathException) exception).getMessage());
                }
                if(errorListener!=null) {
                    errorListener.fatalError(exception);
                }
            }
        };

        xsltCompiler.setErrorListener(errorListener1);
        try {
            XsltExecutable compile = xsltCompiler.compile(new RxmlSAXSource(this.resolver, new RxmlInputSource(ResourceAdapter.convert(stylesheet))));
            templ = new TemplatesImpl(compile);
        } catch (SaxonApiException ex) {
            throw new RuntimeException(join(" ", errors), ex);
        }


        TransformerHandler trh = null;
        try {
            TransformerImpl transformer = (TransformerImpl) templ.newTransformer();
            QName initialMode = transformer.getUnderlyingXsltTransformer().getInitialMode();
            if(mode!=null && !mode.equals("")) {
                transformer.getUnderlyingXsltTransformer().setInitialMode(new QName("","",mode));
            }
            trh = transformer.newTransformerHandler();

        } catch (TransformerConfigurationException e1) {
            throw new SAXException(e1);
        }
        TransformerImpl transformer = (TransformerImpl)trh.getTransformer();
        if (map != null) {
            for (Map.Entry<String, Object> kv : map.entrySet()) {
                Object value = kv.getValue();
                if(value instanceof ResourceFactory)  {
                    value = toDocument(transformer, (ResourceFactory)value);
                }
                transformer.setParameter(kv.getKey(), value);
            }
        }

        SAXResult sr = new SAXResult(ch);
        if(sourceInfo!=null) {
            sr.setSystemId(sourceInfo.toString());
        }
        if (ch instanceof LexicalHandler) {
            sr.setLexicalHandler((LexicalHandler) ch);
        }
        trh.setResult(sr);
        SourceInfo sourceInfo = document.getSourceInfo();
        if(sourceInfo!=null) {
            trh.setSystemId(sourceInfo.toString());
        }
        document.runOn(trh);

    }

    protected NodeInfo toDocument(TransformerImpl transformer, ResourceFactory factory) {
        Controller controller = transformer.getUnderlyingController();
        Builder builder = controller.makeBuilder();
        builder.setBaseURI(null);
        builder.setTiming(false);
        ReceivingContentHandler rch = new ReceivingContentHandler();
        rch.setPipelineConfiguration(new PipelineConfiguration(transformer.getConfiguration()));
        rch.setReceiver(builder);
        builder.open();
        try {
            factory.createSaxResource().runOn(rch);
            builder.close();
            return builder.getCurrentRoot();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (XPathException e) {
            throw new RuntimeException(e);
        }
    }

    private String join(String s, List<String> errors) {
        StringBuilder sb = new StringBuilder();
        String sep="";
        for(String e : errors) {
            sb.append(sep); sep=s;
            sb.append(e);
        }
        return sb.toString();
    }
}



