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
