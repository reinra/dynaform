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
public class PrettyXmlAppender implements XmlVisitor, ElementVisitor {

  private static final String NEW_LINE = "\n";
  private static final String TAB = "  ";
  
	private final StringBuilder sb;
	
	private final PrettyXmlAppender parent;
	private final String element;
	private final int depth;
	
	private boolean hasBody = false;
	
	private boolean newLineAdded = false;
	private boolean hasSubElements = false;
	
	public static XmlVisitor newInstance() {
		return new PrettyXmlAppender();
	}
	
	private PrettyXmlAppender() {
		this(new StringBuilder(), null, null, 0);
	}
	
	private PrettyXmlAppender(StringBuilder sb, String element, PrettyXmlAppender parent, int depth) {
		this.sb = sb;
		this.element = element;
		this.parent = parent;
		this.depth = depth;
	}

  public XmlVisitor text(String s) {
    if (hasSubElements) {
      if (!newLineAdded) {
        sb.append(NEW_LINE);
        newLineAdded = true;
      }
      sb.append(tabs(depth));
      sb.append(s);
      sb.append(NEW_LINE);
    }
    else
      sb.append(s);
    return this;
  }
  
	public ElementVisitor visitElement(String element) {
    if (!newLineAdded && depth > 0) {
      sb.append(NEW_LINE);
      newLineAdded = true;
    }
	  sb.append(tabs(depth));
		sb.append("<").append(element);
		hasSubElements = true;
		return new PrettyXmlAppender(sb, element, this, depth + 1);
	}

  public XmlVisitor visitEndElement() {
    if (!hasBody) {
      sb.append("/>");
      sb.append(NEW_LINE);
    }
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

	public PrettyXmlAppender visitEndBody() {
	  if (hasSubElements)
	    sb.append(tabs(depth - 1));
		sb.append("</").append(element).append(">");
		sb.append(NEW_LINE);
		return this;
	}
	
	public String toString() {
		return sb.toString();
	}
	
	private static String tabs(int count) {
	  StringBuilder sb = new StringBuilder(TAB.length() * count);
	  for (int i = 0; i < count; i++)
	    sb.append(TAB);
	  return sb.toString();
	}

}
