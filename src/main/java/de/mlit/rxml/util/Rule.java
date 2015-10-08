package de.mlit.rxml.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Rule {
	
	


	static enum PatternType {
        ON_START_TAG,
        ON_END_TAG,
        ON_TEXT;
	}
	
	protected PatternType type;
	protected Method method;
    protected String pattern;
	protected boolean absolute;
    protected int[] arguments;
    protected boolean wildcard;

    public static final int ARG_ANNOTIATION_HDL= 1;
    public static final int ARG_ATTRIBUTES = 2;
	
	public Rule(Method method, StartTag tag) {		
		this.type = PatternType.ON_START_TAG;
		this.method = method;

        this.pattern = tag.pattern();

        this.wildcard = pattern.endsWith("*");
        if(wildcard) {
            this.pattern = pattern.substring(0, pattern.length()-1);
        }

        this.absolute = pattern.startsWith("/");
        if(!absolute) {
            this.pattern = "/"+pattern;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        int n = parameterTypes.length;
        arguments = new int[n];
        for(int i=0; i<n; i++) {
            if(parameterTypes[i]==AnnotationHandler.class) {
                arguments[i] = ARG_ANNOTIATION_HDL;
            } else if (parameterTypes[i] == Attributes.class) {
                arguments[i] = ARG_ATTRIBUTES;
            } else {
                throw new RuntimeException("Illegal type:"+parameterTypes[i].getName() +" in " + method.getName());
            }
        }
	}
	
	public Rule (Method method, EndTag tag) {
        this.type = PatternType.ON_END_TAG;
        this.method = method;

        this.pattern = tag.pattern();

        this.wildcard = pattern.endsWith("*");
        if(wildcard) {
            this.pattern = pattern.substring(0, pattern.length()-1);
        }

        this.absolute = pattern.startsWith("/");
        if(!absolute) {
            this.pattern = "/"+pattern;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        int n = parameterTypes.length;
        arguments = new int[n];
        for(int i=0; i<n; i++) {
            if(parameterTypes[i]==AnnotationHandler.class) {
                arguments[i] = ARG_ANNOTIATION_HDL;
            } else {
                throw new RuntimeException("Illegal type:"+parameterTypes[i].getName());
            }
        }
	}


    public Rule (Method method, EventTag tag) {
        this.type = PatternType.ON_END_TAG;
        this.method = method;

        this.pattern = tag.pattern();

        this.wildcard = pattern.endsWith("*");
        if(wildcard) {
            this.pattern = pattern.substring(0, pattern.length()-1);
        }

        this.absolute = pattern.startsWith("/");
        if(!absolute) {
            this.pattern = "/"+pattern;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        int n = parameterTypes.length;
        arguments = new int[n];
        for(int i=0; i<n; i++) {

            if(parameterTypes[i]==AnnotationHandler.class) {
                arguments[i] = ARG_ANNOTIATION_HDL;
            } else if (parameterTypes[i] == Attributes.class) {
                arguments[i] = ARG_ATTRIBUTES;
            } else {
                throw new RuntimeException("Illegal type:"+parameterTypes[i].getName());
            }
        }
    }
	
	public boolean matches(String currentPath) {
        if(wildcard) {
            int idx=currentPath.lastIndexOf("/");
            currentPath = currentPath.substring(0, idx+1);
        }
		if(absolute) {
			return currentPath.equals(pattern);
		} else {
			return currentPath.endsWith(pattern);
		}		
	}
	
	public boolean call(Object recipient, AnnotationHandler hdl) throws SAXException {
            System.out.println("Call "+method.getName());
			try {
                int n = arguments.length;
                if(n >0) {
                    Object args[] = new Object[n];
                    for(int i=0; i<n; i++) {
                        switch(arguments[i]) {
                            case ARG_ANNOTIATION_HDL: args[i] = hdl; break;
                            case ARG_ATTRIBUTES: args[i] = hdl.getAttributes(); break;
                            default: throw new RuntimeException("Not possible");
                        }
                    }
					Object result = method.invoke(recipient, args);
                    return (!Boolean.FALSE.equals(result));
				} else {
					Object result = method.invoke(recipient);
                    return (!Boolean.FALSE.equals(result));
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);				
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);				
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				if(cause instanceof RuntimeException) {
					throw (RuntimeException)cause;
				}
				if(cause instanceof SAXException) {
					throw (SAXException)cause;
				}
				throw new SAXException((Exception)cause);			
			}
	}
	 
}
