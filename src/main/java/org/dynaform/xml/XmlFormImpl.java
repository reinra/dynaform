package org.dynaform.xml;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.reader.XmlReader;
import org.dynaform.xml.writer.XmlWriter;


public class XmlFormImpl implements XmlForm {

	private final Form form;
	private final XmlWriter writer;
	private final XmlReader reader;
	
	public XmlFormImpl(Form form, XmlWriter writer) {
	  this(form, writer, null);
	}

	public XmlFormImpl(Form form, XmlWriter writer, XmlReader reader) {
	  this.form = form;
	  this.writer = writer;
	  this.reader = reader;
	}
	
	public Form getForm() {
		return form;
	}

	public XmlWriter getWriter() {
		return writer;
	}
	
	public XmlReader getReader() {
    return reader;
  }

  @Override
	public String toString() {
		return "XML Form {form: " + form + ", " + "writer: " + writer + ", reader: " + reader + "}";
	}
	
}
