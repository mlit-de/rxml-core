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

package de.mlit.rxml.base;

import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;

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
        file.getParentFile().mkdirs();

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
