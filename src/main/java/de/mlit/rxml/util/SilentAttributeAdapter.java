package de.mlit.rxml.util;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Created by mlauer on 01/05/15.
 */
public class SilentAttributeAdapter {

    ContentHandler delegate;

    public SilentAttributeAdapter(ContentHandler delegate) {
        this.delegate = delegate;
    }

    protected AttributesImpl attributes = new AttributesImpl();



    public void startDocument() {
        try {
            delegate.startDocument();
            attributes.clear();
        } catch(SAXException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addAttributeIfNotNull(String name, String value) {
        if(value!=null) {
            attributes.addAttribute("", name, name, "", value);
        }
    }

    public void addAttribute(String name, String value) {
        attributes.addAttribute("", name, name, "", value);
    }

    public String qName(String prefix, String name) {
        return (prefix != null && !("".equals(prefix))) ? prefix+":"+name : name;
    }

    public void addAttribute(String uri, String prefix, String name, String value) {

        attributes.addAttribute(uri, name, qName(prefix, name), "", value);
    }

    public void addAttribute(String name, int value) {
        attributes.addAttribute("", name, name, "", ""+value);
    }


    public void startElement(String name) {
        try {
            delegate.startElement("", name, name, attributes);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        attributes.clear();
    }

    public void startFqElement(String uri, String pf, String name) {
        try {
            delegate.startElement(uri, name, qName(pf, name), attributes);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        attributes.clear();
    }

    public void endElement(String name) {
        try {
            delegate.endElement("", name, name);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void endFqElement(String uri, String pf, String name) {
        try {
            delegate.endElement(uri, name, qName(pf, name));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    public void emptyElement(String name) {
        startElement(name);
        endElement(name);
    }

    public void text(String text) {
        try {
            delegate.characters(text.toCharArray(), 0, text.length());
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }


}
