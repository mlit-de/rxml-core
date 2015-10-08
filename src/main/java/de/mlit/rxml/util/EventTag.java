package de.mlit.rxml.util;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 26/02/13

 * To change this template use File | Settings | File Templates.
 */
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventTag {
    String pattern();
}
