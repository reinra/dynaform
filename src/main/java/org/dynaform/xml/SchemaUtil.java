package org.dynaform.xml;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Rein Raudjärv
 * 
 * @see XmlUtil
 */
public class SchemaUtil {
  
  static final Log log = LogFactory.getLog(SchemaUtil.class);

  /**
   * Reads an XML Schema from a given file.
   * 
   * @param file input file.
   * @return XML Schema representation.
   */
  public static XSSchemaSet parse(final File file) {
    XSSchemaSet result;
    try {
      XSOMParser parser = new XSOMParser();
      parser.setErrorHandler(new ErrorHandler() {
        public void error(SAXParseException e) throws SAXException {
          log.error("Error occured while parsing '" + file + "'", e);
        }
        public void fatalError(SAXParseException e) throws SAXException {
          log.error("Fatal error occured while parsing '" + file + "'", e);
        }
        public void warning(SAXParseException e) throws SAXException {
          log.error("Warning occured while parsing '" + file + "'", e);
        }
      });
      parser.parse(file);
      result = parser.getResult();
    }
    catch (Exception e) {
      throw new RuntimeException("Failed to parse schema '" + file + "'", e);
    }
    if (result == null)
      throw new RuntimeException("Failed to parse schema '" + file + "'");
    return result;
  }

  /**
   * Converts an XML Schema into an abstract form with XML data.
   * 
   * @param set XML Schema representation.
   * @return an abstract form with XML data.
   */
  public static XmlForm toXmlForm(XSSchemaSet set) {
    return XmlFormBuilder.build(set);
  }
  
  /**
   * Converts an XML Schema into an abstract form with XML data.
   * 
   * @param set XML Schema representation.
   * @return an abstract form with XML data.
   */
  public static XmlForm toXmlForm(XSSchemaSet set, String element) {
    return XmlFormBuilder.build(set, element);
  }

  /**
   * Produces an abstract form with XML data from the given XML Schema file.
   * <p>
   * Same as <code>SchemaUtil.toXmlForm(SchemaUtil.toSchemaSet(file))</code>.
   * 
   * @param file an XML Schema file.
   * @return an abstract form with XML data.
   */
  public static XmlForm toXmlForm(File file) {
    return toXmlForm(parse(file));
  }
  
  /**
   * Produces an abstract form with XML data from the given XML Schema file.
   * <p>
   * Same as <code>SchemaUtil.toXmlForm(SchemaUtil.toSchemaSet(file))</code>.
   * 
   * @param file an XML Schema file.
   * @return an abstract form with XML data.
   */
  public static XmlForm toXmlForm(File file, String element) {
    return toXmlForm(parse(file), element);
  }

}
