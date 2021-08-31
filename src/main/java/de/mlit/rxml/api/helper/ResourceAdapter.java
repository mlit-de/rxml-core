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

import de.mlit.rxml.api.ResourceFactory;
import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.SourceInfo;
import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.resolver.RxmlResolver;
import org.xml.sax.EntityResolver;

import java.util.Map;

public class ResourceAdapter {

    public void setSourceInfo(SourceInfo sourceInfo) {

    }

    public static ResourceFactory convert(final SaxResource resource) {
        return new ResourceFactory() {

            public SaxResource createSaxResource() {
                return resource;
            }


            public StreamResource createStreamResource() {
                SerializedXml serializedXml = new SerializedXml(resource);
                serializedXml.setSourceInfo(resource.getSourceInfo());
                return serializedXml;
            }


        };
    }


    public static ResourceFactory convert(final StreamResource resource, final EntityResolver resolver) {
        return new ResourceFactory() {
            @Override
            public SaxResource createSaxResource() {
                ParsedStream parsedStream = new ParsedStream(resource, resolver);
                parsedStream.setSourceInfo(resource.getSourceInfo());
                return parsedStream;
            }

            @Override
            public StreamResource createStreamResource() {
                return resource;
            }


        };
    }

    public static ResourceFactory convert(final StreamResource resource) {
        return new ResourceFactory() {
            @Override
            public SaxResource createSaxResource() {
                ParsedStream parsedStream = new ParsedStream(resource);
                parsedStream.setSourceInfo(resource.getSourceInfo());
                return parsedStream;
            }

            @Override
            public StreamResource createStreamResource() {
                return resource;
            }


        };
    }

    public static ResourceFactory convert(final ResourceFactory factory) {
        return factory;
    }


    public static StreamResource toStream(ResourceFactory factory) {
        return factory.createStreamResource();
    }

    public static SaxResource toSax(ResourceFactory factory) {
        return factory.createSaxResource();
    }


    public static ResourceFactory getInput(Map map, String name) {
        final Object rf = map.get(name);
        if(rf==null) {
            throw new NullPointerException("No such Input "+name);
        }
        return (ResourceFactory) rf;
    }

    public static RxmlResolver getResolver(Map map) {
        return (RxmlResolver)map.get("#resolver");
    }
}
