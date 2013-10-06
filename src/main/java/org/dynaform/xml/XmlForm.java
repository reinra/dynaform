package org.dynaform.xml;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.reader.XmlReader;
import org.dynaform.xml.writer.XmlWriter;




/**
 * Abstract form representation with XML data.
 * 
 * @author Rein Raudjärv
 */
public interface XmlForm {

	/**
	 * @return general form.
	 */
	Form getForm();
	
	/**
	 * @return XML writer.
	 */
	XmlWriter getWriter();
	
	/**
	 * @return XML reader.
	 */
	XmlReader getReader();
	
}
