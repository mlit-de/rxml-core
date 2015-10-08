package de.mlit.rxml.util;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Created by mlauer on 03/03/15.
 */
public class AttributeAdapter extends DelegatingContentHandler {

	public AttributeAdapter(ContentHandler delegate) {
		super(delegate);
	}
	
	protected AttributesImpl attributes = new AttributesImpl();

	
	@Override
	public void startDocument() throws SAXException {	
		super.startDocument();
		attributes.clear();
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

	
	public void startElement(String name) throws SAXException {
		this.startElement("", name, name, attributes);
		attributes.clear();
	}

    public void startFqElement(String uri, String pf, String name) throws SAXException {
        this.startElement(uri, name, qName(pf, name), attributes);
        attributes.clear();
    }

    public void endElement(String name) throws SAXException {
		this.endElement("", name, name);		
	}

    public void endFqElement(String uri, String pf, String name) throws SAXException {
        this.endElement(uri, name, qName(pf, name));
    }
	
	public void emptyElement(String name) throws SAXException {
		startElement(name);
		endElement(name);
	}
	
	public void text(String text) throws SAXException {
		this.characters(text.toCharArray(), 0, text.length());
	}

	

}
