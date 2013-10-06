package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;

/**
 * @author Rein Raudjärv
 * 
 * @see XmlWriter
 * @see TextWriter
 * @see XmlTextHandler
 */
public class TextXmlWriter implements XmlWriter {

  private final TextWriter textWriter;
  
  public TextXmlWriter(TextWriter textWriter) {
    this.textWriter = textWriter;
  }
  
  public TextWriter getWriter() {
    return textWriter;
  }

  public boolean isEmpty() {
    return textWriter.getText() == null;
  }

  public void write(XmlVisitor xv) {
    xv.text(textWriter.getText());
  }

}
