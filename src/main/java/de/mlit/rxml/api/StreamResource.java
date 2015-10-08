package de.mlit.rxml.api;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 05/10/13
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public interface StreamResource extends Resource {

    public Reader openReader() throws IOException;

    public InputStream openStream() throws IOException;

    public void writeOn(OutputStream os) throws IOException;

    public void writeOn(Writer writer) throws IOException;

    public CharSequence getContent() throws IOException;

    public String getContentAsString() throws IOException;

}
