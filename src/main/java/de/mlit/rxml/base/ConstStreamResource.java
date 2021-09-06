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

import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Created by mlauer on 28/02/15.
 */
public class ConstStreamResource extends AbstractStreamResource {

    protected byte[] content;

    public ConstStreamResource(String content) {
        this(content.getBytes());
    }

    public ConstStreamResource(byte[] content) { this.content = content; }

    public ConstStreamResource() {
    }

    @Override
    public String getContent() {
        return new String(content);
    }

    public byte[] getContentBytes() {
        return content;
    }

    public void setContent(String content) {
        this.content = content.getBytes();
    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromInputStream();
    }

    @Override
    public String getContentAsString()  {
        return getContent();
    }

    @Override
    public InputStream openStream() throws IOException {
        return new ByteArrayInputStream(content);
    }
}
