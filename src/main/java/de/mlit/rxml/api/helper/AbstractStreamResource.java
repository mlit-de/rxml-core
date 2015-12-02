package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.StreamResource;

import java.io.*;
import java.util.Base64;

/**
 * Created by mlauer on 03/03/15.
 */
public abstract class AbstractStreamResource extends AbstractResource implements StreamResource {

    protected static int BUFFER_SIZE = 4096;


    @Override
    public abstract Reader openReader() throws IOException;

    protected Reader openReaderFromContent() throws IOException {
        return new StringReader(getContentAsString());
    }

    protected Reader openReaderFromInputStream() throws IOException {
        return new InputStreamReader(openStream());
    }


    protected InputStream bufferAndReadOutputStream() throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream os2 = Base64.getMimeEncoder().wrap(os);
        this.writeOn(os2);
        os2.flush();
        os2.close();
        return new ByteArrayInputStream(os.toByteArray());
    }

    @Override
    public InputStream openStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeOn(baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }


    @Override
    public void writeOn(OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os);
        writeOn(writer);
        writer.flush();
    }




    @Override
    public void writeOn(Writer writer) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];
        Reader reader = openReader();
        int n = 0;
        while ((n = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, n);
        }
        reader.close();
    }

    @Override
    public CharSequence getContent() throws IOException {
        StringWriter writer = new StringWriter();
        writeOn(writer);
        return writer.getBuffer();
    }

    public String getContentAsString() throws IOException {
        return getContent().toString();
    }
}
