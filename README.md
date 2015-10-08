# rxml-core

This library provides some abstractions to process XML Files as stream of characters or via SAX interface.

To transform a file `document.xml` by a stylesheet `transform.xslt` you can just use:


       new SerializedXml(
                new XsltResource(
                        new ParsedStream(new FileResource("transform.xslt")),
                        new ParsedStream(new FileResource("document.xml")),
                        null, null)).writeOn(System.out);


