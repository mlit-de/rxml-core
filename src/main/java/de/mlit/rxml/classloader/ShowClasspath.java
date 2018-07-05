package de.mlit.rxml.classloader;


import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by mlauer on 22/04/16.
 */
public class ShowClasspath {
    public static void main(String args[]) throws Exception {
        String file = args[0] + "/.classpath";
        System.err.println("Writing:" + file);
        FileOutputStream fos = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(fos);
        String sep = System.getProperty("path.separator");
        String[] split = args[1].split(sep);
        for(String s : split) {
            pw.println(s);
            System.out.println(s);
        }
        pw.flush();
        fos.flush();
        fos.close();
    }
}
