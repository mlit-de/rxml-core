package de.mlit.rxml.resolver;

import de.mlit.rxml.api.Resource;
import de.mlit.rxml.api.ResourceFactory;
import org.xml.sax.InputSource;

import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Created by mlauer on 08/10/15.
 */
public class RxmlInputSource extends InputSource {

    protected ResourceFactory resource;

    public RxmlInputSource(ResourceFactory resource) {
        this.resource = resource;
    }

    @Override
    public void setByteStream(InputStream byteStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getByteStream() {
        try {
            return resource.createStreamResource().openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
