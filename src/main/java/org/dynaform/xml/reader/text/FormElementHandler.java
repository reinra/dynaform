package org.dynaform.xml.reader.text;

import org.dynaform.xml.form.FormElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FormElementHandler implements TextHandler {

  private static final Log log = LogFactory.getLog(FormElementHandler.class);
  
  private final FormElement<?> element;

  public FormElementHandler(FormElement<?> element) {
    if (element == null)
      throw new IllegalArgumentException();
    
    this.element = element;
  }
  
  public FormElement<?> getElement() {
    return element;
  }
  
  public void text(String s) {
    if (s == null)
      log.debug("Received null for " + element);
    else
      log.info("Received text '" + s + "' for " + element);
      
    element.setXmlValue(s);
  }
  
  public String toString() {
    return FormElementHandler.class.getSimpleName() + " '" + element.getFullSelector() + "'";
  }

}
