package org.dynaform.xml;

import org.dynaform.xml.reader.InvalidXmlException;
import org.dynaform.xml.reader.XmlParser;
import org.dynaform.xml.reader.XmlReader;
import org.dynaform.xml.writer.PrettyXmlAppender;
import org.dynaform.xml.writer.XmlAppender;
import org.dynaform.xml.writer.XmlWriter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Rein Raudjärv
 * 
 * @see XmlVisitor
 * @see XmlParser
 * @see DefaultHandler 
 */
public class XmlUtil {
	
  /**
   * Uses an XmlWriter to produce a pretty XML String. 
   * 
   * @param form an abstract form 
   * @return an XML String.
   */
  public static String writeXml(XmlWriter writer) {
    try {
      XmlVisitor xa = PrettyXmlAppender.newInstance();
      writer.write(xa);
      return xa.toString();
    } catch (Exception e) {
      throw new RuntimeException("Failed to produce XML String from XML writer '" + writer + "'", e);
    }
  }
  
  /**
   * Uses an XmlWriter to produce an XML String without additional whitespace. 
   * 
   * @param form an abstract form 
   * @return an XML String.
   */
  public static String writeCompactXml(XmlWriter writer) {
    try {
      XmlVisitor xa = XmlAppender.newInstance();
      writer.write(xa);
      return xa.toString();
    } catch (Exception e) {
      throw new RuntimeException("Failed to produce XML String from XML writer '" + writer + "'", e);
    }
  }
	
	/**
	 * Parses XML String using the given {@link XmlReader}.
	 * 
	 * @param xml XML String.
	 * @param xv an XML reader.
	 */
	public static void readXml(String xml, XmlReader xr) {
    try {
      readXml(xml, xr.create(null));
    } catch (InvalidXmlException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Failed to read XML String '" + xml + "' using XML reader '" + xr + "'", e);
    }
	}
	
	/**
	 * Parses XML String using the given {@link XmlVisitor}.
	 * 
	 * @param xml XML String.
	 * @param xv an XML visitor.
	 */
	private static void readXml(String xml, XmlVisitor xv) {
	  parse(xml, new XmlParser(xv));
	}
	
	/**
	 * Parses XML String using the given SAX event handler.
	 * 
	 * @param xml XML String.
	 * @param handler SAX event handler.
	 */
	private static void parse(String xml, DefaultHandler handler) {
	  if (xml == null || "".equals(xml))
	    return;
	  
    try {
      InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser;
      saxParser = factory.newSAXParser();
      saxParser.parse(in, handler);
    } catch (Exception e) {
      if (e instanceof RuntimeException)
        throw (RuntimeException) e;
      throw new RuntimeException("Failed to parse XML String '" + xml + "'", e);
    }
	}
  
  /**
   * Uses {@link XmlParser} and {@link XmlAppender}
   * to read and write an XML String. 
   * 
   * @param xml XML String to be read.
   * @return XML String written.
   */
  public static String readAndWrite(String xml) {
    XmlVisitor xv = XmlAppender.newInstance();
    readXml(xml, xv);
    return xv.toString();
  }
	
}
