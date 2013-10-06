package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.XmlVisitor.ElementVisitor;


public class EndBodyReader implements XmlReader {

  private final ElementVisitor visitor;

  public EndBodyReader(ElementVisitor elementVisitor) {
    this.visitor = elementVisitor;
  }
  
  public XmlVisitor create(XmlReader next) {
    return new EndBodyVisitor(next);
  }
  
  public String toString() {
    return EndBodyReader.class.getSimpleName() + "{" + visitor + "}";
  }
  
  private class EndBodyVisitor extends BaseXmlVisitor {
    
    public EndBodyVisitor(XmlReader next) {
      super(next);
    }
    
    public ElementVisitor visitEndBody() {
      return visitor;
    }
    
    public String toString() {
      return EndBodyVisitor.class.getSimpleName() + "{" + visitor + "}";
    }
    
  }
  
}
