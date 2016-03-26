package de.mlit.rxml.resolver;

import de.mlit.rxml.api.ResourceFactory;
import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.helper.ResourceAdapter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

/**
 * Created by mlauer on 08/10/15.
 */
public abstract class RxmlResolver implements EntityResolver, URIResolver {



    public InputSource wrapInputSource(ResourceFactory resourceFactory, String systemId) {
        RxmlInputSource rxmlInputSource = new RxmlInputSource(resourceFactory);
        rxmlInputSource.setSystemId(systemId);
        return rxmlInputSource;
    }

    public SAXSource wrapSource(ResourceFactory resourceFactory)  {
        return new RxmlSAXSource(this, new RxmlInputSource(resourceFactory));

    }

    public Source wrapSource(SaxResource input) {
        return wrapSource(ResourceAdapter.convert(input));
    }


}
