package de.mlit.rxml.base;

import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 27/10/13
 * Time: 09:22
 * To change this template use File | Settings | File Templates.
 */
public class FileResource extends AbstractStreamResource {

    protected File file;

    public File getFile() {
        return file;
    }

    public FileResource(File file) {
        this.file = file;
    }

    public FileResource(String name) {
        this.file = new File(name);
    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromInputStream();
    }

    @Override
    public InputStream openStream() throws IOException {
        return new FileInputStream(file);
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
