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
import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.base.FileResource;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class ParsedStream extends AbstractResource implements SaxResource {

    protected StreamResource parent;
    protected EntityResolver resolver;


    public ParsedStream(StreamResource parent) {
        this.parent = parent;
    }

    public ParsedStream(StreamResource parent, EntityResolver resolver) {
        super();
        this.parent = parent;
        this.resolver = resolver;
    }

    public void runOn(ContentHandler ch) throws SAXException, IOException {
        XMLReader reader = createXmlReader(resolver);
        reader.setContentHandler(ch);
        InputStream inputStream = parent.openStream();
        InputSource is = new InputSource(inputStream);
        if(parent instanceof FileResource) {
            URI uri = ((FileResource)parent).getFile().getAbsoluteFile().toURI();
            is.setSystemId(uri.toString());
        }
        try {
            reader.parse(is);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                // fall through
            }
        }

    }

    public static XMLReader createXmlReader(EntityResolver resolver) throws SAXException {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        if(resolver!=null) {
            xmlReader.setEntityResolver(resolver);
        }
        return xmlReader;
    }
}
