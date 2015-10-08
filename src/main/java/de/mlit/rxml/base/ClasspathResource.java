package de.mlit.rxml.base;

import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;

/**
 * Created by mlauer on 28/02/15.
 */
public class ClasspathResource extends AbstractStreamResource implements StreamResource {

    protected String name;

    public ClasspathResource(String name) {
        this.name = name;
    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromInputStream();
    }

    @Override
    public InputStream openStream() throws IOException {
        String name2 = this.name.startsWith("/") ? this.name.substring(1) : this.name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name2);
    }
}
