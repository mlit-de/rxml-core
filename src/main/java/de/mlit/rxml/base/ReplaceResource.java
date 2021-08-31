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

import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.helper.AbstractResource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

public class ReplaceResource extends AbstractResource implements SaxResource {

    protected SaxResource first;
    protected SaxResource second;

    public ReplaceResource(SaxResource first, SaxResource second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void runOn(ContentHandler ch) throws SAXException, IOException {
        first.runOn(new DefaultHandler());
        second.runOn(ch);
    }
}
