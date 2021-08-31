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

package de.mlit.rxml.resolver;

import org.xml.sax.*;

import javax.xml.transform.sax.SAXSource;
import java.io.IOException;

public class RxmlSAXSource extends SAXSource {

    public RxmlSAXSource(RxmlResolver resolver, RxmlInputSource inputSource) {
        super(new RxmlReader(resolver), inputSource);
    }



    static class RxmlReader implements XMLReader {

        protected EntityResolver resolver;
        protected ErrorHandler errorHandler;

        public RxmlReader(EntityResolver resolver) {
            this.resolver = resolver;
        }

        protected ContentHandler contentHandler;

        @Override
        public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return false;
        }

        @Override
        public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {

        }

        @Override
        public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
            return null;
        }

        @Override
        public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {

        }

        @Override
        public void setEntityResolver(EntityResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        public void setDTDHandler(DTDHandler handler) {
        }

        @Override
        public DTDHandler getDTDHandler() {
            return null;
        }

        @Override
        public void setContentHandler(ContentHandler handler) {
            this.contentHandler = handler;
        }

        @Override
        public ContentHandler getContentHandler() {
            return contentHandler;
        }

        @Override
        public void setErrorHandler(ErrorHandler handler) {
            this.errorHandler = handler;
        }

        @Override
        public ErrorHandler getErrorHandler() {
            return errorHandler;
        }

        @Override
        public void parse(String systemId) throws IOException, SAXException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void parse(InputSource input) throws IOException, SAXException {
            ((RxmlInputSource)input).resource.createSaxResource().runOn(getContentHandler());
        }

        @Override
        public EntityResolver getEntityResolver() {
            return resolver;
        }
    }

}
