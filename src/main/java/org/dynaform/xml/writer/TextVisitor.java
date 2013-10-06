package org.dynaform.xml.writer;

/**
 * Event handler for text.
 * 
 * @author Rein Raudjärv
 */
public interface TextVisitor {

	/**
	 * Handle text.
	 * 
	 * @param s text.
	 */
  TextVisitor text(String s);
	
}
