package org.dynaform.xml.reader;

/**
 * @author Rein Raudjärv
 */
public class InvalidXmlException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public InvalidXmlException() {
    super();
  }
  
  public InvalidXmlException(String message) {
    super(message);
  }
  
}
