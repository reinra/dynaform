package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BeforeReader implements XmlReader {
  
  private static final Log log = LogFactory.getLog(BeforeReader.class); 

  private final XmlReader reader;
  private final Runnable handler;
  
  public BeforeReader(XmlReader reader, Runnable handler) {
    this.reader = reader;
    this.handler = handler;
  }
  
  public XmlVisitor create(XmlReader next) {
    return new BeforeVisitor(reader.create(next));
  }
  
  public String toString() {
    return BeforeReader.class.getSimpleName() + "(" + handler + ") {" + reader + "}";
  }
  
  private class BeforeVisitor implements XmlVisitor {

    private final XmlVisitor visitor;
    
    public BeforeVisitor(XmlVisitor visitor) {
      this.visitor = visitor;
      handler.run();
    }
    
    public XmlVisitor text(String s) {
      return visitor.text(s);
    }

    public ElementVisitor visitElement(String element) {
      return visitor.visitElement(element);
    }

    public ElementVisitor visitEndBody() {
      return visitor.visitEndBody();
    }
    
  }

}
