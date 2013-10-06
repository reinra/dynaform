package org.dynaform.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

public class CountLines {

  interface FileCallback {
    void handle(File file);
  }
  
  public static void main(String[] args) {
    File dir = new File(".").getAbsoluteFile();
    System.out.println(dir);
    
    count(dir, ".java");
    count(dir, ".jsp");
  }
  
  static void count(File dir, String suffix) {
    LineCounter counter = new LineCounter();
    traverse(dir, new ExtensionFilter(suffix), counter);
    int lines = counter.lines;
    System.out.println(suffix + " lines: " + lines);
  }
  
  static void traverse(File dir, FileFilter filter, FileCallback callback) {
    if (dir == null || !dir.isDirectory())
      return;
      
    File[] list = dir.listFiles(filter);
    if (list == null)
      return;
    
    for (int i = 0; i < list.length; i++) {
      File file = list[i];
      if (file.isDirectory())
        traverse(file, filter, callback);
      else {
//        System.out.println(file);
        callback.handle(file);
      }
    }
  }
  
  static class ExtensionFilter implements FileFilter {
    
    private final String suffix;
    
    public ExtensionFilter(String suffix) {
      this.suffix = suffix;
    }
    
    public boolean accept(File pathname) {
      return pathname.isDirectory()
          || pathname.getName().endsWith(suffix);
    }
    
  }
  
  static class LineCounter implements FileCallback {
    
    private int lines;

    public void handle(File file) {
      try {
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while ((line = in.readLine()) != null) {
          if (!isEmpty(line))
            lines++;
        }
        in.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    private boolean isEmpty(String line) {
      String trim = line.trim();
      return trim.length() == 0
          || trim.startsWith("//")
          || trim.startsWith("/*")
          || trim.startsWith("*")
          || trim.startsWith("*/");
    }
    
  }

}
