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

package de.mlit.rxml.api.helper;

import de.mlit.rxml.api.StreamResource;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.io.IOException;


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
