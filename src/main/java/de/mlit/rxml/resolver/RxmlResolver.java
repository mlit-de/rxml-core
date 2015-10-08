package de.mlit.rxml.resolver;

import de.mlit.rxml.api.ResourceFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;

/**
 * Created by mlauer on 08/10/15.
 */
public abstract class RxmlResolver implements EntityResolver, URIResolver {



    public InputSource wrapInputSource(ResourceFactory resourceFactory, String systemId) {
        RxmlInputSource rxmlInputSource = new RxmlInputSource(resourceFactory);
        rxmlInputSource.setSystemId(systemId);
        return rxmlInputSource;
    }

    public Source wrapSource(ResourceFactory resourceFactory) throws SAXException {
        return new RxmlSAXSource(this, new RxmlInputSource(resourceFactory));

    }


}
