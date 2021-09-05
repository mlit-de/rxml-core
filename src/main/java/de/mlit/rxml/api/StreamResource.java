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

package de.mlit.rxml.api;

import java.io.*;

public interface StreamResource extends Resource {

    Reader openReader() throws IOException;

    InputStream openStream() throws IOException;

    void writeOn(OutputStream os) throws IOException;

    void writeOn(Writer writer) throws IOException;

    CharSequence getContent() throws IOException;

    String getContentAsString() throws IOException;

}
