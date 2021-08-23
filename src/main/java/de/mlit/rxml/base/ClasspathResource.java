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
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name2);
        if(inputStream==null) {
            throw new IOException("Can't find on classpath: "+name2);
        }
        return inputStream;
    }
    @Override
    public void writeOn(OutputStream os) throws IOException {
        byte[] buffer = new byte[4096];
        InputStream inputStream = this.openStream();
        try {
            int n=0;
            while((n=inputStream.read(buffer))>0) {
                os.write(buffer,0,n);
            }
        } finally {
            inputStream.close();
        }
        os.flush();
    }
}
