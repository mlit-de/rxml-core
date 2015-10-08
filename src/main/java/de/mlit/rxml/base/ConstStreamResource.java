package de.mlit.rxml.base;

import de.mlit.rxml.api.helper.AbstractStreamResource;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by mlauer on 28/02/15.
 */
public class ConstStreamResource extends AbstractStreamResource {

    protected String content;

    public ConstStreamResource(String content) {
        this.content = content;
    }

    public ConstStreamResource() {
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Reader openReader() throws IOException {
        return openReaderFromContent();
    }

    @Override
    public String getContentAsString() throws IOException {
        return content;
    }
}
