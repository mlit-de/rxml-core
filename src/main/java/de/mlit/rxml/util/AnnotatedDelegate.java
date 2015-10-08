package de.mlit.rxml.util;

import org.xml.sax.SAXException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mlauer
 * Date: 25/02/13
 * Time: 09:45
 * To change this template use File | Settings | File Templates.
 */
public class AnnotatedDelegate {

    public Object delegate;

    public List<Rule> rules;

    public AnnotatedDelegate(Object delegate, List<Rule> rules) {
        this.delegate = delegate;
        this.rules = rules;
    }

    public void call(Rule rule, AnnotationHandler hdl) throws SAXException {
        rule.call(delegate, hdl);
    }


    public boolean callRules(AnnotationHandler annotationHandler, Rule.PatternType type, String currentPath) throws SAXException {
        for (Rule r : rules) {
            if (r.type == type) {
                if (r.matches(currentPath)) {
                    if(r.call(delegate, annotationHandler)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
