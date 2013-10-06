package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AfterReader implements XmlReader {
  
  private static final Log log = LogFactory.getLog(AfterReader.class); 

  private final XmlReader reader;
  private final Runnable handler;
  
  public AfterReader(XmlReader reader, Runnable handler) {
    this.reader = reader;
    this.handler = handler;
  }
  
  public XmlVisitor create(XmlReader next) {
    return new AfterVisitor(reader.create(next));
  }
  
  public String toString() {
    return AfterReader.class.getSimpleName() + "(" + handler + ") {" + reader + "}";
  }
  
  private class AfterVisitor implements XmlVisitor {

    private final XmlVisitor visitor;
    
    public AfterVisitor(XmlVisitor visitor) {
      this.visitor = visitor;
    }
    
    public XmlVisitor text(String s) {
      XmlVisitor result = visitor.text(s);
      handler.run();
      return result;
    }

    public ElementVisitor visitElement(String element) {
      ElementVisitor result = visitor.visitElement(element);
      handler.run();
      return result;
    }

    public ElementVisitor visitEndBody() {
      ElementVisitor result = visitor.visitEndBody();
      handler.run();
      return result;
    }
    
  }

}
