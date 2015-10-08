package de.mlit.rxml.api;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 05/10/13
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public interface SaxResource extends Resource {

    public void runOn(ContentHandler ch) throws SAXException, IOException;

}
