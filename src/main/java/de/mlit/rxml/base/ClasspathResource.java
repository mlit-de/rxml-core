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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

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
