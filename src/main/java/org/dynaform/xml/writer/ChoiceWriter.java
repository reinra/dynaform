package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormChoice;

import java.util.Collections;
import java.util.List;

/**
 * @author Rein Raudjärv
 * 
 * @see FormChoice
 * @see XmlWriter
 */
public class ChoiceWriter implements FormChoice.ChoiceChangeListener, XmlWriter {

  private final List<XmlWriter> children;
  private XmlWriter selectedChild;

  public ChoiceWriter(FormChoice form, List<XmlWriter> children) {
    this.children = Collections.unmodifiableList(children);
    onSetSelectedIndex(form.getSelectedIndex());
    form.addListener(this);
  }
  
  public void onSetSelectedIndex(int index) {
    selectedChild = children.get(index);
  }

  public boolean isEmpty() {
    return selectedChild.isEmpty();
  }

  public void write(XmlVisitor xv) {
    selectedChild.write(xv);
  }

}