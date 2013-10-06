package org.dynaform.xml;


/**
 * XML event handler.
 * 
 * @author Rein Raudjärv
 */
public interface XmlVisitor {

  /**
   * Handle text.
   * 
   * @param s text.
   * @return XML event handler to be used as next.
   */
  XmlVisitor text(String s);

  /**
   * Handle XML element start.
   * 
   * @param element name of the XML element.
   * @return event handler for the XML element.
   */
  ElementVisitor visitElement(String element);
  
  /**
   * Handle end of the XML element body.
   * 
   * @return event handler of the XML element to be used as next.
   */
  ElementVisitor visitEndBody();

  /**
   * Event handler of XML element.
   * 
   * @author Rein Raudjärv
   */
  interface ElementVisitor {
    /**
     * Handle XML attribute.
     * 
     * @param name name of the XML attribute.
     * @param value value of the XML attribute.
     * @return event handler for the XML element.
     */
    ElementVisitor visitAttribute(String name, String value);

    /**
     * Handle start of the XML body.
     * 
     * @return event handler for the XML element body.
     */
    XmlVisitor visitBody();

    /**
     * Handle end of the XML element.
     * 
     * @return XML event handler to be used as next.
     */
    XmlVisitor visitEndElement();
  }

}
