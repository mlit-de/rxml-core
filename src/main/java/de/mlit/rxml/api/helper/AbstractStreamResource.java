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

package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.StreamResource;

import java.io.*;


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
