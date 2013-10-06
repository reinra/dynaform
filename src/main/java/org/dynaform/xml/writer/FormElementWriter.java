package org.dynaform.xml.writer;

import org.dynaform.xml.form.FormElement;


/**
 * @author Rein Raudjärv
 */
public class FormElementWriter implements TextWriter {

	private final FormElement<?> element;
	
	public FormElementWriter(FormElement<?> element) {
	  if (element == null)
	    throw new IllegalArgumentException();
	  
		this.element = element;
	}
	
	public String getText() {
	  return element.getXmlValue();
	}

  public String toString() {
    return FormElementWriter.class.getSimpleName() + " '" + element.getFullSelector() + "'";
  }

}
