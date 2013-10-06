package org.dynaform.xml.writer;

import org.dynaform.xml.XmlVisitor;
import org.dynaform.xml.form.FormAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rein Raudjärv
 * 
 * @see FormAll
 * @see XmlWriter
 */
public class AllWriter implements FormAll.OnChangeListener, XmlWriter {

  private final List<XmlWriter> children;
  private final List<XmlWriter> selectedChildren = new ArrayList<XmlWriter>();
  
  public AllWriter(FormAll form, List<XmlWriter> children) {
    this.children = Collections.unmodifiableList(children);
    onSetSelectedIndexes(form.getSelectedIndexes());
    form.addListener(this);
  }
  
  public void onSetSelectedIndexes(int[] indexes) {
    selectedChildren.clear();
    for (int i = 0; i < indexes.length; i++)
      selectedChildren.add(children.get(indexes[i]));
  }
  
  public boolean isEmpty() {
    return selectedChildren.isEmpty();
  }

  public void write(XmlVisitor xv) {
    for (XmlWriter child : selectedChildren)
      child.write(xv);
  }

}
