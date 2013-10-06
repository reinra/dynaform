package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MaybeReader implements XmlReader {
  
  private static final Log log = LogFactory.getLog(MaybeReader.class); 

  private final XmlReader reader;
  private final XmlReader fallback;
  
  public MaybeReader(XmlReader reader, XmlReader fallback) {
    this.reader = reader;
    this.fallback = fallback;
  }
  
  public XmlVisitor create(XmlReader next) {
    return new MaybeVisitor(reader.create(next));
  }
  
  public String toString() {
    return MaybeReader.class.getSimpleName() + "{" + reader + " or " + fallback + "}";
  }
  
  private class MaybeVisitor implements XmlVisitor {

    private final XmlVisitor visitor;
    
    public MaybeVisitor(XmlVisitor visitor) {
      this.visitor = visitor;
    }
    
    public XmlVisitor text(String s) {
      if (s == null || "".equals(s))
        return this;
      
      XmlVisitor result;
      log.info("Received text '" + s + "'");
      try {
        result = visitor.text(s);
      }
      catch (InvalidXmlException e) {
        log.error(e);
        result = fallback.create(null).text(s);
      }
      log.info("After text '" + s + "' returned " + result);
      return result;
    }

    public ElementVisitor visitElement(String element) {
      log.info("Received element '" + element + "'");
      ElementVisitor result;
      try {
        result = visitor.visitElement(element);
      }
      catch (InvalidXmlException e) {
        log.error(e);
        result = fallback.create(null).visitElement(element);
      }
      log.info("After element '" + element + "' returned " + result);
      return result;
    }

    public ElementVisitor visitEndBody() {
      log.info("Received end of body");
      ElementVisitor result;
      try {
        result = visitor.visitEndBody();
      }
      catch (InvalidXmlException e) {
        log.error(e);
        result = fallback.create(null).visitEndBody();
      }
      log.info("After end of body returned " + result);
      return result;
    }
    
  }

}
