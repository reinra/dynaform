package org.dynaform.xml.reader;


import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.XmlVisitor.ElementVisitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Delegates SAX events to an {@link XmlVisitor}.
 * 
 * @author Rein Raudjärv
 * 
 * @see XmlVisitor
 */
public class XmlParser extends DefaultHandler {

	private static final Log log = LogFactory.getLog(XmlParser.class);

	/**
	 * Visitor of the current XML element body. 
	 */
	private XmlVisitor visitor;
	
	/**
	 * Text buffer.
	 */
	private final StringBuilder sb = new StringBuilder();
	
	// position of the start element
	// if the position of the end element is the same an empty element is detected
	/**
	 * Line number of the start element.
	 */
	private int lineNumber;
	
  /**
   * Column number of the start element.
   */
	private int columnNumber;
	
	/**
	 * Location in the document.
	 */
	private Locator locator;

	/**
	 * Creates new instance of {@link XmlParser}.
	 * 
	 * @param documentVisitor visitor for the XML root element.
	 */
	public XmlParser(XmlVisitor documentVisitor) {
	  this.visitor = documentVisitor;
	}
	
	public void setDocumentLocator(Locator locator) {
		this.locator = locator; 
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		// Process texts
		processText();
		
		// Remember the position of the start element
		savePosition();
		
		log.info("Start element '" + qName + "' (" + getPosition() + ") -> " + visitor);
		  
		ElementVisitor elementVisitor;
		try {
		  elementVisitor = visitor.visitElement(qName);
		  if (elementVisitor == null)
		    throw new NullPointerException();
		} catch (RuntimeException e) {
			log.error("Error occured while handling start of element '" + qName + "': " + e.getMessage(), e);
			throw e;
		}
		  
	  elementVisitor = attributes(qName, elementVisitor, attributes);
	  visitor = new LazyBodyVisitor(elementVisitor);
	}

  private ElementVisitor attributes(String elementQName, ElementVisitor elementVisitor, Attributes attributes) {
    for (int i = 0; i < attributes.getLength(); i++) {
    	String name = attributes.getLocalName(i);
    	String value = attributes.getValue(i);
    	
    	log.info("Attribute '" + name + "' (" + getPosition() + ") -> " + elementVisitor);
    	
    	try {
    		elementVisitor.visitAttribute(name, value);
    	} catch (RuntimeException e) {
    		log.error("Error occured while handling start attribute '" + name +
    				"' of element '" + elementQName + "': " + e.getMessage(), e);
    		throw e;
    	}
    }
    return elementVisitor;
  }

	public void endElement(String uri, String localName, String qName) {
		// Process texts
	  if (!isSamePosition())
	    processText();
		
		log.info("End element '" + qName + "' (" + getPosition() + ") -> " + visitor);
		
		ElementVisitor elementVisitor;
		try {
		  elementVisitor = visitor.visitEndBody();
		} catch (RuntimeException e) {
			log.error("Error occured while handling end body of element '" + qName + "': " + e.getMessage(), e);
			throw e;
		}
		this.visitor = elementVisitor.visitEndElement();
	}
	
	public void characters(char ch[], int start, int length) {
		sb.append(ch, start, length);
	}
	
	private void processText() {
		String text = sb.toString();
		sb.setLength(0);
		
		try {
			this.visitor = visitor.text(text); // invoke even with empty String
		} catch (RuntimeException e) {
			log.error("Error occured while handling text '" + text + "': " + e.getMessage(), e);
			throw e;
		}
	}
	
	private void savePosition() {
		lineNumber = locator.getLineNumber();
		columnNumber = locator.getColumnNumber();
	}
	
	private boolean isSamePosition() {
		return lineNumber == locator.getLineNumber()
			&& columnNumber == locator.getColumnNumber();
	}
	
	private String getPosition() {
	  return locator.getLineNumber() + ":" + locator.getColumnNumber();
	}
	
}
