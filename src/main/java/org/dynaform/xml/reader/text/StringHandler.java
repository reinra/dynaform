package org.dynaform.xml.reader.text;

public class StringHandler implements TextHandler {

  private String value;
  
  public void text(String s) {
    value = s;
  }
  
  public String getValue() {
    return value;
  }
  
  public String toString() {
    return StringHandler.class.getSimpleName();
  }

}
