package org.dynaform.xml.writer;

import org.dynaform.xml.RepeatAdapter;
import org.dynaform.xml.XmlForm;
import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormRepeat;

/**
 * @author Rein Raudjärv
 * 
 * @see FormRepeat
 * @see RepeatAdapter
 * @see XmlWriter
 */
public class RepeatWriter implements XmlWriter {

  private final RepeatAdapter<?> adapter;

  public RepeatWriter(RepeatAdapter<?> adapter) {
    this.adapter = adapter;
  }

  public boolean isEmpty() {
    return adapter.getChildren().isEmpty();
  }

  public void write(XmlVisitor xa) {
    for (XmlForm child : adapter.getChildren().values())
      child.getWriter().write(xa);
  }

}