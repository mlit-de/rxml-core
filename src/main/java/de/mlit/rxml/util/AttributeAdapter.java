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

package de.mlit.rxml.util;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

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
