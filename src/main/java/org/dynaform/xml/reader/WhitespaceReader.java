package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Rein Raudjärv
 */
public class WhitespaceReader implements XmlReader {
  
  private static final Log log = LogFactory.getLog(WhitespaceReader.class);
  
  private static final XmlReader INSTANCE = new WhitespaceReader();
  
  public static XmlReader getIInstance() {
    return INSTANCE;
  }
  
  public static XmlReader prependTo(XmlReader reader) {
    return SequenceReader.newInstance(INSTANCE, reader);
  }
  
  public static XmlReader appendTo(XmlReader reader) {
    return SequenceReader.newInstance(reader, INSTANCE);
  }
  
  private WhitespaceReader() {
  }

  public XmlVisitor create(XmlReader next) {
    return new WhitespaceVisitor(next);
  }
  
  public String toString() {
    return WhitespaceReader.class.getSimpleName();
  }
  
  private class WhitespaceVisitor extends BaseXmlVisitor {
    
    public WhitespaceVisitor(XmlReader next) {
      super(next);
    }
    
    public XmlVisitor text(String s) {
      if (isWhitespace(s)) {
        if (s.length() > 0)
          log.debug("Skipping whitespace of length " + s.length());
        
        return this;
      }
      return nextVisitor().text(s);
    }

    public ElementVisitor visitElement(String element) {
      return nextVisitor().visitElement(element);
    }

    public ElementVisitor visitEndBody() {
      return nextVisitor().visitEndBody();
    }
    
    public String toString() {
      return WhitespaceVisitor.class.getSimpleName() + "(" + nextReader() + ")";
    }
    
  }
  
  private static boolean isWhitespace(String s) {
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!Character.isWhitespace(c))
        return false;
    }
    return true;
  }

}
