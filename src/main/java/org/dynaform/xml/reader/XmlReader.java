package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

/**
 * XML Reader.
 * 
 * @author Rein Raudjärv
 * 
 * @see XmlVisitor
 */
public interface XmlReader {

  /**
   * Creates XML event handler for consuming the XML.
   * 
   * @param next next XML reader.
   * @return XML event handler.
   */
  XmlVisitor create(XmlReader next);
  
}
