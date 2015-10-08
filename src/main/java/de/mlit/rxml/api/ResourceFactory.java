package de.mlit.rxml.api;

/**
 * Created by mlauer on 04/03/15.
 */
public interface ResourceFactory {

    public SaxResource createSaxResource();

    public StreamResource createStreamResource();

}
