package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

public class BaseXmlVisitor implements XmlVisitor {
  
  private final XmlReader nextReader;
  
  public BaseXmlVisitor(XmlReader next) {
    this.nextReader = next;
  }
  
  protected XmlReader nextReader() {
    return nextReader;
  }
  
  protected XmlVisitor nextVisitor() {
    return nextReader == null ? null : nextReader.create(null);
  }

  public XmlVisitor text(String s) {
    if (s == null || "".equals(s))
      return this;
    throw new InvalidXmlException("Text not supported");
  }

  public ElementVisitor visitElement(String element) {
    throw new InvalidXmlException("Element '" + element + "' not supported");
  }

  public ElementVisitor visitEndBody() {
    throw new InvalidXmlException("End of body not supported");
  }

}
