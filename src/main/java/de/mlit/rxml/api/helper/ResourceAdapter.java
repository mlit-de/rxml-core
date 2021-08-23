package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.*;
import de.mlit.rxml.resolver.RxmlResolver;
import org.xml.sax.EntityResolver;

import java.util.Map;

/**
 * Created by mlauer on 04/03/15.
 */
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
