package org.dynaform.xml.reader.text;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.reader.BaseXmlVisitor;
import org.dynaform.xml.reader.XmlReader;

/**
 * @author Rein Raudjärv
 * 
 * @see TextHandler
 */
public class TextXmlReader implements XmlReader {

  private final TextHandler handler;
  
  public TextXmlReader(TextHandler handler) {
    this.handler = handler;
  }
  
  public TextHandler getHandler() {
    return handler;
  }

  public XmlVisitor create(XmlReader next) {
    return new TextVisitor(next);
  }
  
  public String toString() {
    if (handler instanceof FormElementHandler) {
      FormElement<?> element = ((FormElementHandler) handler).getElement();
      return "FormElementReader '" + element.getFullSelector() + "'";
    }
    return TextXmlReader.class.getSimpleName() + " {" + handler + "}";
  }
  
  private class TextVisitor extends BaseXmlVisitor {
    
    public TextVisitor(XmlReader next) {
      super(next);
      handler.text(null);
    }
    
    public XmlVisitor text(String s) {
      handler.text(s);
      return nextVisitor();
    }
    
  }
  
}
