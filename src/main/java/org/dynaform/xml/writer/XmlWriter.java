package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;

/**
 * XML writer.
 * <p>
 * Generates an XML document by accepting an {@link XmlVisitor}.
 * </p>
 * <p>
 * To get an XML string use {@link XmlAppender} as the visitor.
 * 
 * @author Rein Raudjärv
 * 
 * @see XmlAppender
 * @see #write(XmlVisitor)
 * @see #isEmpty()
 */
public interface XmlWriter {
	
	/**
	 * @return <code>true</code> if this writer is empty.
	 */
	boolean isEmpty();
	
	/**
	 * Writes XML by accepting the given {@link XmlVisitor}.
	 * 
	 * @param xv XML visitor.
	 */
	void write(XmlVisitor xv);
	
}
