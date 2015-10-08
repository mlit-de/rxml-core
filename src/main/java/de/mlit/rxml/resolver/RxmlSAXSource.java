package de.mlit.rxml.resolver;

import org.xml.sax.*;

import javax.xml.transform.sax.SAXSource;
import java.io.IOException;

/**
 * Created by mlauer on 08/10/15.
 */
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
