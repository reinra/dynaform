package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.XmlVisitor.ElementVisitor;

/**
 * XML event handler for building XML String.
 * 
 * @author Rein Raudjärv
 * 
 * @see XmlVisitor
 */
public class XmlAppender implements XmlVisitor, ElementVisitor {

	private final StringBuilder sb;
	private final String element;
	
	private boolean hasBody = false;
	
	private final XmlAppender parent;
	
	public static XmlVisitor newInstance() {
		return new XmlAppender();
	}
	
	private XmlAppender() {
		this(new StringBuilder(), null, null);
	}
	
	private XmlAppender(StringBuilder sb, String element, XmlAppender parent) {
		this.sb = sb;
		this.element = element;
		this.parent = parent;
	}

  public XmlVisitor text(String s) {
    sb.append(s);
    return this;
  }
  
	public ElementVisitor visitElement(String element) {
		sb.append("<").append(element);
		return new XmlAppender(sb, element, this);
	}

  public XmlVisitor visitEndElement() {
    if (!hasBody)
      sb.append("/>");
    return parent;
  }
  
  public ElementVisitor visitAttribute(String name, String value) {
    sb.append(" ").append(name).append("=\"").append(value).append("\"");
    return this;
  }
  
	public XmlVisitor visitBody() {
		sb.append(">");
		hasBody = true;
		return this;
	}

	public XmlAppender visitEndBody() {
		sb.append("</").append(element).append(">");
		return this;
	}
	
	public String toString() {
		return sb.toString();
	}

}
