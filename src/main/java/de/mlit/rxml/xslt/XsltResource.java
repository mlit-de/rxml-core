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

import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.helper.AbstractResource;
import net.sf.saxon.Configuration;
import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.lib.FeatureKeys;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class XsltResource extends AbstractResource implements SaxResource {


    protected SaxResource stylesheet;
    protected SaxResource document;

    protected Map<String, Object> map;

    //protected static SAXTransformerFactory factory = (SAXTransformerFactory)TransformerFactory.newInstance();

    protected SAXTransformerFactory factory;

    public XsltResource(SaxResource stylesheet, SaxResource document,
                        Map<String, Object> map, URIResolver resolver, List<AbstractSaxonExtension> extensions) {
        this.stylesheet = stylesheet;
        this.document = document;
        this.map = map;
        TransformerFactoryImpl saxonFactory = new TransformerFactoryImpl();

        this.factory = saxonFactory;
        factory.setAttribute(FeatureKeys.XSLT_VERSION, "2.0");
        factory.setURIResolver(resolver);
        if(extensions != null) {
            Configuration conf = saxonFactory.getConfiguration();
            for(AbstractSaxonExtension ext : extensions) {
                if(ext != null) {
                    ext.install(conf);
                }
            }
        }

    }



    @Override
    public void runOn(ContentHandler ch) throws SAXException, IOException {
        TemplatesHandler th;
        try {
            th = factory.newTemplatesHandler();
            //String systemId = stylesheet.getSystemId();
            //if (systemId != null) {
            //    th.setSystemId(systemId);
            //}
            stylesheet.runOn(th);
            Templates templ = th.getTemplates();
            TransformerHandler trh = factory.newTransformerHandler(templ);
            if (map != null) {
                for (Map.Entry<String, Object> kv : map.entrySet()) {
                    trh.getTransformer().setParameter(kv.getKey(), kv.getValue());
                }
            }
            SAXResult sr = new SAXResult(ch);
            if(ch instanceof LexicalHandler) {
                sr.setLexicalHandler((LexicalHandler)ch);
            }
            trh.setResult(sr);
            document.runOn(trh);

        } catch (TransformerConfigurationException e) {
            throw new SAXException(e);
        }
    }


}
