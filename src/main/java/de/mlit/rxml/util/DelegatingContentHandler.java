/*
 * Copyright (C) 2015-2021 Markus Lauer
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

package de.mlit.rxml.util;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class DelegatingContentHandler implements ContentHandler {

    protected ContentHandler delegate;

    public DelegatingContentHandler(ContentHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        delegate.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        delegate.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        delegate.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        delegate.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        delegate.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        delegate.startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        delegate.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        delegate.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        delegate.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        delegate.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        delegate.skippedEntity(name);
    }
}
