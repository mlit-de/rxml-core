package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.base.FileResource;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 05/10/13
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
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
