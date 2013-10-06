package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LazyBodyVisitor implements XmlVisitor {
  
  private static final Log log = LogFactory.getLog(LazyBodyVisitor.class);

  private final ElementVisitor elementVisitor;
  private XmlVisitor bodyVisitor;
  
  public LazyBodyVisitor(ElementVisitor elementVisitor) {
    this.elementVisitor = elementVisitor;
  }
  
  public XmlVisitor text(String s) {
    check();
    log.debug("Received text '" + s + "'");
    return bodyVisitor.text(s);
  }
  
  public ElementVisitor visitElement(String element) {
    check();
    log.debug("Received element '" + element + "'");
    return bodyVisitor.visitElement(element);
  }

  public ElementVisitor visitEndBody() {
    log.debug("Received end of body");
    return elementVisitor;
  }

  private void check() {
    if (bodyVisitor == null)
      bodyVisitor = elementVisitor.visitBody();
  }
  
  public String toString() {
    return LazyBodyVisitor.class.getSimpleName() + "(" + elementVisitor + ")";
  }

}
