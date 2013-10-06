package org.dynaform.xml.reader;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.XmlVisitor.ElementVisitor;
import org.dynaform.xml.reader.text.NopHandler;
import org.dynaform.xml.reader.text.TextHandler;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XmlElementReader implements XmlReader {

  private static final Log log = LogFactory.getLog(XmlElementReader.class);
  
  private final String elemenetName;
  private final Map<String, TextHandler> attributes;
  private final XmlReader body;
  
  public XmlElementReader(String name, Map<String, TextHandler> attributes, XmlReader body) {
    this.elemenetName = name;
    this.attributes = attributes;
    this.body = body;
  }
  
  public XmlVisitor create(XmlReader next) {
    return new XmlElementVisitor(next);
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(XmlElementReader.class.getSimpleName());
    sb.append("('");
    sb.append(elemenetName);
    sb.append("')");
    
    if (attributes == null || attributes.isEmpty()) {
      if (body != null) {
        sb.append(" {");
        sb.append(body);
        sb.append("}");
      }
    }
    else {
      sb.append(" {");
      sb.append(attributes);
      sb.append(" ");
      if (body != null) {
        sb.append("body={");
        sb.append(body);
        sb.append("}");
      }
      sb.append("}");
    }
    return sb.toString();
  }
  
  private class XmlElementVisitor extends BaseXmlVisitor implements ElementVisitor {
    
    private XmlVisitor bodyVisitor;
    
    public XmlElementVisitor(XmlReader next) {
      super(next);
    }
    
    public ElementVisitor visitElement(String element) {
      if (!elemenetName.equals(element))
        throw new InvalidXmlException("Element '" + element + "' not supported, '" + elemenetName + "' expected");
      
      log.debug("Received element '" + element + "'");
      
      if (body != null)
        bodyVisitor = body.create(new EndBodyReader(this));
      return this;
    }

    public ElementVisitor visitAttribute(String name, String value) {
      TextHandler handler = attributes == null ? null : attributes.get(name);
      if (handler == null) {
        if (shouldIgnoreAttribute(name, value))
          handler = NopHandler.INSTANCE;
        else
          throw new InvalidXmlException("Attribute '" + name + "' not supported");
      }
      handler.text(value);
      log.debug("Received elemenet '" + elemenetName + "' attribute '" + name + "' value '" + value + "'");
      return this;
    }

    public XmlVisitor visitBody() {
      if (body == null)
        throw new InvalidXmlException("Body not supported");
      log.debug("Received body of element '" + elemenetName + "'");
      return bodyVisitor;
    }

    public XmlVisitor visitEndElement() {
      log.debug("Received end of element '" + elemenetName + "'");
      XmlVisitor result = nextVisitor();
      return result;
    }
    
    public String toString() {
      return XmlElementVisitor.class.getSimpleName() + " '" + elemenetName + "'";
    }
    
  }
  
  private static boolean shouldIgnoreAttribute(String name, String value) {
    return name.startsWith("xmlns") || name.startsWith("xsi:");
  }

}
