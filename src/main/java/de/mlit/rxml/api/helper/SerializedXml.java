/*
 * Copyright (C) 2013-2021 Markus Lauer
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

package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.SaxResource;
import org.xml.sax.SAXException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class SerializedXml extends AbstractStreamResource {

    public static final int NO_INDENT = -1;

    protected String method;
    protected SaxResource parent;
    protected int indent = NO_INDENT;
    protected String doctypePublic = null;
    protected String doctypeSystem = null;
    protected boolean omitXmlDeclaration = true;


    public void setIndent(int indent) {
        this.indent = indent;
    }

    public SerializedXml withIndent(int indent) {
        SerializedXml result = new SerializedXml(parent);
        result.setIndent(indent);
        return result;
    }


    public SerializedXml(SaxResource parent) {
        super();
        this.parent = parent;
    }

    public SerializedXml(SaxResource parent, int indent, String method) {
        this.parent = parent;
        this.indent = indent;
        this.method = method;
    }


    public void setDoctypePublic(String doctypePublic) {
        this.doctypePublic = doctypePublic;
    }

    public void setDoctypeSystem(String doctypeSystem) {
        this.doctypeSystem = doctypeSystem;
    }

    public void setOmitXmlDeclaration(boolean omitXmlDeclaration) {
        this.omitXmlDeclaration = omitXmlDeclaration;
    }

    protected TransformerHandler createTransformerHandler() {
        SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        TransformerHandler th;
        try {
            th = factory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        Transformer tf = th.getTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, (indent >=0) ? "yes" : "no");
        if(indent >=0 ) {
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent+"");
            //tf.setOutputProperty("{http://saxon.sf.net/}indent-spaces", indent+"");
        }
        tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
        if(doctypePublic != null) {
            tf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypePublic);
        }
        if(doctypeSystem != null) {
            tf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctypeSystem);
        }

        if(method != null) {
            tf.setOutputProperty(OutputKeys.METHOD, method);
        }
        return th;
    }

    @Override
    public void writeOn(OutputStream os) throws IOException {
        try {
            TransformerHandler th = createTransformerHandler();
            th.setResult(new StreamResult(os));
            parent.runOn(th);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeOn(Writer writer) throws IOException {
        try {
            TransformerHandler th = createTransformerHandler();
            th.setResult(new StreamResult(writer));
            parent.runOn(th);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromContent();
    }
}
