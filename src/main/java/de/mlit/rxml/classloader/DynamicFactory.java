package de.mlit.rxml.classloader;


import java.beans.Expression;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mlauer on 22/04/16.
 */
public class DynamicFactory {


    public static URL[] getURLs(String classPathFile) throws IOException {
        List<URL> result = new ArrayList<URL>();
        File configFile = new File(classPathFile).getAbsoluteFile();
        FileInputStream fileInputStream = new FileInputStream(configFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
        String line;
        while((line = reader.readLine())!=null) {
            line = line.trim();
            if(line.length()==0 || line.startsWith("#")) {
                continue;
            }

            File file = new File(line);
            if(!file.isAbsolute()) {
                file = new File(configFile.getParent(), line);
            }
            URL url = file.getCanonicalFile().toURI().toURL();
            result.add(url);
            //System.out.println("<"+url+">");
        }
        return result.toArray(new URL[result.size()]);
    }

    public static Object invoke(String classPathFile, String className, String name, Object... args) throws Exception {
        Class clazz = loadClass(classPathFile, className);
        return new Expression(clazz,name,args).getValue();
    }

    public static Object instantiate(String classPathFile, String className, Object... args) throws Exception {
        Class clazz = loadClass(classPathFile, className);

        try {
            return new Expression(clazz, "new", args).getValue();
        } catch(NoSuchMethodException ex) {
            StringBuilder sb = new StringBuilder();
            String sep="";
            for(Object arg : args) {
                sb.append(sep); sep=",";
                if(arg==null) {
                    sb.append("null");
                }
                sb.append(arg.getClass().getName());
            }
            System.err.println(className+"::<new>("+sb.toString()+")");
            throw ex;
        }
    }


    protected static Class loadClass(String classPathFile, String className) throws IOException, ClassNotFoundException {
        URL[] urls = getURLs(classPathFile);
        URLClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        URL resource = cl.findResource(className.replace(".", "/") + ".class");
        //System.out.println("Found: "+resource);
        Class<?> aClass = cl.loadClass(className);
        return aClass;

    }

}
