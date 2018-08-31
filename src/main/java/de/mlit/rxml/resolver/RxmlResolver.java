package de.mlit.rxml.resolver;

import de.mlit.rxml.api.ResourceFactory;
import de.mlit.rxml.api.SaxResource;
import de.mlit.rxml.api.helper.ResourceAdapter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;


import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import java.net.URI;
import java.net.URISyntaxException;

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

    public URI normalizeUri(String href, String base) {
        try {
            URI uri = new URI(href);
            if (!uri.isAbsolute()) {
                uri = new URI(base).resolve(uri);
            }
            if (!uri.isAbsolute()) {
                throw new RuntimeException("Uri is not absolute" + uri.toString());
            }
            return uri;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract ResourceFactory resolveToResourceFactory(URI uri);

}
