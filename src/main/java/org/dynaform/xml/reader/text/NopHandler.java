package org.dynaform.xml.reader.text;

/**
 * @author Rein Raudjärv
 */
public class NopHandler implements TextHandler {

  public static final TextHandler INSTANCE = new NopHandler();
  
  private NopHandler() {
  }
  
  public void text(String s) {
  }
  
  public String toString() {
    return NopHandler.class.getSimpleName();
  }

}
