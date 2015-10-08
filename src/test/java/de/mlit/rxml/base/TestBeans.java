package de.mlit.rxml.base;

import de.mlit.rxml.api.ResourceFactory;
import de.mlit.rxml.api.helper.BeansDelegate;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mlauer on 07/03/15.
 */
public class TestBeans {

    @Test
    public void test() throws Exception {
        Map<String,Object> context = new HashMap();
        //context.put("path", "FOO");
        ClasspathResource resource = new ClasspathResource("de/mlit/rxml/base/bean.xml");

        //context.put("abc", resource);
        ResourceFactory s = BeansDelegate.beans(resource, ResourceFactory.class, context);
        Assert.assertEquals("<foo>bar</foo>", s.createStreamResource().getContentAsString());

    }
}
