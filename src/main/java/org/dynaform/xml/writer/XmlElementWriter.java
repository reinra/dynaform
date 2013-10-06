package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.XmlVisitor.ElementVisitor;

import java.util.Map;
import java.util.Map.Entry;

public class XmlElementWriter implements XmlWriter {

	private final String name;
	private final Map<String, TextWriter> attributes;
	private final XmlWriter body;
	
	public XmlElementWriter(String name, Map<String, TextWriter> attributes, XmlWriter body) {
		this.name = name;
		this.attributes = attributes;
		this.body = body;
	}
	
	public boolean isEmpty() {
		return false;
	}

	public void write(XmlVisitor xa) {
		ElementVisitor elementVisitor = xa.visitElement(name);
		
		// Attributes
		if (attributes != null) {
			for (Entry<String, TextWriter> entry : attributes.entrySet()) {
				String attrName = entry.getKey();
				TextWriter textWriter = entry.getValue();
				String value = textWriter.getText();
				if (value != null)
				  elementVisitor.visitAttribute(attrName, value);
			}
		}
		
		// Body
		if (body != null && !body.isEmpty()) {
			XmlVisitor bodyVisitor = elementVisitor.visitBody();
			body.write(bodyVisitor);
			bodyVisitor.visitEndBody();
		}
		
		elementVisitor.visitEndElement();
	}
	
  public String toString() {
    StringBuilder sb = new StringBuilder();
    if (attributes == null) {
      sb.append(XmlElementWriter.class.getSimpleName());
      sb.append("('");
      sb.append(name);
      sb.append("')");
      if (body != null) {
        sb.append(" {");
        sb.append(body);
        sb.append("}");
      }
    }
    else {
      sb.append(XmlElementWriter.class.getSimpleName());
      sb.append("('");
      sb.append(name);
      sb.append("') {");
      if (attributes != null) {
        sb.append(attributes);
        sb.append(" ");
      }
      if (body != null) {
        sb.append(" body={");
        sb.append(body);
        sb.append("}");
      }
      sb.append("}");
    }
    return sb.toString();
  }
  
	
}
