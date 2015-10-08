package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.Resource;
import de.mlit.rxml.api.SourceInfo;

/**
 * Created by mlauer on 27/02/15.
 */
public class AbstractResource implements Resource {

    protected SourceInfo sourceInfo;

    public void setSourceInfo(SourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    @Override
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
