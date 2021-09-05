/*
 * Copyright (C) 2013-2021 Markus Lauer
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

import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.*;

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
        try(InputStream inputStream = this.openStream()) {
            copyContent(inputStream, os);
        }
        os.flush();
    }
}
