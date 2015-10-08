package de.mlit.rxml.util;


import java.lang.reflect.Method;
import java.util.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class AnnotationHandler extends DefaultHandler {


    //----------------------------------------------

    protected Map<Class, List<Rule>> rulesMap = new HashMap<Class, List<Rule>>();

    protected AnnotatedDelegate wrapDelegate(Object delegate) {
        Class<?> clazz = delegate.getClass();
        final List<Rule> rules;

        if (!rulesMap.containsKey(clazz)) {
            rules  = initRules(clazz);
            rulesMap.put(clazz, rules);
        } else {
            rules = rulesMap.get(clazz);
        }
        return new AnnotatedDelegate(delegate, rules);
    }


    protected List<Rule> initRules(Class clazz) {
        List<Rule> result = new ArrayList<Rule>();
        for (Method m : clazz.getMethods()) {
            StartTag st = m.getAnnotation(StartTag.class);
            if (st != null) {
                result.add(new Rule(m, st));
            }
            EndTag et = m.getAnnotation(EndTag.class);
            if (et != null) {
                result.add(new Rule(m, et));
            }
            EventTag evt = m.getAnnotation(EventTag.class);
            if (evt != null) {
                result.add(new Rule(m, evt));
            }
        }
        //System.out.println("Init rules for "+clazz.getSimpleName()+": "+result.size());
        return result;
    }

    protected List<AnnotatedDelegate> delegates = new ArrayList<AnnotatedDelegate>();

    public AnnotationHandler(Object... delegates) {
        for (Object dlg : delegates) {
            this.delegates.add(wrapDelegate(dlg));
        }
    }

    public void addDelegate(Object delegate, boolean prio) {
        if(prio) {
            this.delegates.add(0,wrapDelegate(delegate));
        } else {
            this.delegates.add(wrapDelegate(delegate));
        }
    }

    public void fireRules(Rule.PatternType type) throws SAXException {
        System.out.println(currentPath + ":" + type);
        for (AnnotatedDelegate dlg : this.delegates) {
            if(dlg.callRules(this, type, currentPath)) {
                return;
            }
        }
    }


    //-------------------------

    protected Stack<Object> contextStack = new Stack();
    protected int contextDepth = 0;

    public void pushConext(Object... delegates) {
        contextStack.push(this.delegates);
        contextStack.push(contextDepth);
        this.delegates=new ArrayList<AnnotatedDelegate>();
        for(Object dlg: delegates) {
            this.delegates.add(wrapDelegate(dlg));
        }
        contextDepth=0;

    }
    protected void popContext() {
        contextDepth = (Integer)contextStack.pop();
        delegates = (List<AnnotatedDelegate>)contextStack.pop();

    }
    protected Stack pathStack = new Stack();
    protected String currentPath;
    protected StringBuffer sb = new StringBuffer();
    protected String lastText;
    protected String elemName = "";

    public String getElemName() {
        return elemName;
    }

    public String getText() {
        return lastText;
    }


    protected Attributes atts;

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        sb.append(ch, start, length);
    }

    protected void flushText() {
        lastText = sb.toString();
        sb = new StringBuffer();
        System.err.println("T<" + lastText +">");
    }

    public void endDocument() throws SAXException {
    }


    public void endElement(String uri, String localName, String qName) throws SAXException {
        elemName = localName == null ? qName : localName;
        flushText();
        if(contextDepth==0) {
            popContext();
        }
        contextDepth-=1;
        fireRules(Rule.PatternType.ON_END_TAG);
        currentPath = (String) pathStack.pop();
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        flushText();
        contextDepth+=1;
        atts = new AttributesImpl(attributes);
        elemName = localName == null ? qName : localName;

        pathStack.push(currentPath);
        currentPath = ((currentPath.length() == 1) ? currentPath : currentPath + "/") + elemName;
        fireRules(Rule.PatternType.ON_START_TAG);
    }



    public void startDocument() {
        currentPath = "/";
    }

    public Attributes getAttributes() {
        return atts;
    }

    public String getAttribute(String name) {
        return atts.getValue(name);
    }

    public int getIntAttribute(String name, int dflt) {
        String value = getAttribute(name);
        if (value == null) {
            return dflt;
        }
        return Integer.parseInt(value);
    }
}
