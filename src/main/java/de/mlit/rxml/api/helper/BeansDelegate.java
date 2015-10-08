package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.StreamResource;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.IOException;

/**
 * Created by mlauer on 03/03/15.
 */
public class BeansDelegate {

    public static <T> T beans(StreamResource resource, Class<T> clazz) {
        return beans(resource, clazz, null);

    }

    public static <T> T beans(StreamResource resource, Class<T> clazz, Object owner) {
        try {
            XMLDecoder xmlDecoder = new XMLDecoder(resource.openStream(), owner);
            xmlDecoder.setExceptionListener(new ExceptionListener() {
                @Override
                public void exceptionThrown(Exception e) {
                    if(e instanceof RuntimeException) {
                        throw (RuntimeException)e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            });
            Object o = xmlDecoder.readObject();
            return (T) o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            try {
                System.err.println(resource.getContentAsString());
            } catch(IOException ex2) {
                //Hier können wir auch nichts mehr machen.
            }
            throw ex;
        }
    }

}
