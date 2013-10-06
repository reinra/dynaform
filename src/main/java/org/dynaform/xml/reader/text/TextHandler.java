package org.dynaform.xml.reader.text;

/**
 * Event handler for text.
 * 
 * @author Rein Raudjärv
 */
public interface TextHandler {

	/**
	 * Handle text.
	 * 
	 * @param s text.
	 */
  void text(String s);
	
}
