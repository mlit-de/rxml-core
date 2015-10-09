package de.mlit.rxml.base;

import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;
import java.util.List;

/**
 * Created by mlauer on 08/10/15.
 */
public class CachedResource extends AbstractStreamResource {

    protected StreamResource parent;
    protected String basedirname;
    protected String filename;
    protected List<String> dependends;

    public CachedResource(StreamResource parent, String basedirname, String filename, List<String> dependends) {
        this.parent = parent;
        this.basedirname = basedirname;
        this.filename = filename;
        this.dependends = dependends;
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
        file.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(file);
        byte[] result = baos.toByteArray();
        fos.write(result);
        fos.flush();
        fos.close();
        return result;
    }

    public boolean needUpdate() throws IOException {
        File dir = new File(basedirname);
        if(!dir.isDirectory()) {
            throw new IOException("File "+basedirname+" is not a directory");
        }
        File file = new File(dir, filename);
        if(file.exists()) {
            long lm = file.lastModified();
            for(String s : dependends) {

                File f = new File(s);
                if(!f.exists() ||  f.lastModified() >= lm) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void writeOn(OutputStream os) throws IOException {
        if(needUpdate()) {
            byte[] buffer = save();
            os.write(buffer);
        } else {
            byte[] buffer = new byte[4096];
            InputStream inputStream = openStream(false);
            try {
                int n=0;
                while((n=inputStream.read(buffer))>0) {
                    os.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            os.flush();
        }
    }


    @Override
    public InputStream openStream() throws IOException {
        return openStream(needUpdate());
    }

    public InputStream openStream(boolean needUpdate) throws IOException {
        if(needUpdate) {
            save();
        }
        File dir = new File(basedirname);
        File file = new File(dir, filename);
        return new FileInputStream(file);

    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromInputStream();
    }


}
