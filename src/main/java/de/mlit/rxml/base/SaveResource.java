package de.mlit.rxml.base;

import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;

/**
 * Created by mlauer on 07/03/15.
 */
public class SaveResource extends AbstractStreamResource {

    protected StreamResource parent;
    protected String basedirname;
    protected String filename;

    public SaveResource(StreamResource parent, String basedirname, String filename) {
        this.parent = parent;
        this.basedirname = basedirname;
        this.filename = filename;
    }

    protected void checkParent(File file, File dir) throws IOException {
        String dirname = dir.getAbsolutePath();
        if(!dirname.endsWith(File.separator)) {
            dirname = dirname + File.separator;
        }
        if(!file.getAbsolutePath().startsWith(dirname)) {
            throw new IOException("File " + file.getAbsolutePath() + " not within "+dir.getAbsolutePath());

        }
    }

    protected byte[] save() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        parent.writeOn(baos);
        File dir = new File(basedirname);
        if(!dir.isDirectory()) {
            throw new IOException("File "+basedirname+" is not a directory");
        }
        File file = new File(dir, filename);
        checkParent(file, dir);

        FileOutputStream fos = new FileOutputStream(file);
        byte[] result = baos.toByteArray();
        fos.write(result);
        fos.flush();
        fos.close();
        return result;
    }

    @Override
    public void writeOn(OutputStream os) throws IOException {
        byte[] buffer = save();
        os.write(buffer);
    }

    @Override
    public InputStream openStream() throws IOException {
        save();
        File dir = new File(basedirname);
        File file = new File(dir, filename);
        return new FileInputStream(file);

    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromInputStream();
    }


}
