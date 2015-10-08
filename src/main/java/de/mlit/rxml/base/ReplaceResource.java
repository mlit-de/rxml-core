package de.mlit.rxml.base;

import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.StreamResource;
import de.mlit.rxml.api.helper.AbstractResource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

/**
 * Created by mlauer on 27/03/15.
 */
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
