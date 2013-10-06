package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormChoice;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.ToggleFormVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Rein Raudjärv
 *
 * @see FormChoice
 */
public class FormChoiceImpl extends BaseForm implements FormChoice, FormChoice.ChoiceChangeListener {

  private String id;
  
  private final List<Form> children;
  private int selectedIndex;
  
  private final Collection<ChoiceChangeListener> listeners = new ArrayList<ChoiceChangeListener>();

  public FormChoiceImpl(List<Form> children) {
    if (children == null || children.size() <= 1)
      throw new IllegalArgumentException("Children: " + children);
    
    this.children = Collections.unmodifiableList(children);
    
    // Select first child by default
    this.selectedIndex = 0;
    
    // Register myself as parent of all the children
    int i = 0;
    for (Form child : children) {
      child._setParent(this);
      
      // Disable all children except the first one
      if (i > 0)
        child.accept(ToggleFormVisitor.DISABLER);
      i++;
    }
  }
  
  public void _setParent(Form parent) {
    super._setParent(parent);
    
    if (id == null)
      id = OPERATOR_CHOICE + INDEX_START + parent.nextCounterValue() + INDEX_END;
  }

  public String getHighLevelId() {
    return null;
  }
  
  public String getLowLevelId() {
    return id;
  }
  
  public List<Form> getChildren() {
    return children;
  }
  
  public Form getSelectedChild() {
    return children.get(selectedIndex);
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public void setSelectedIndex(int index) {
    if (index < 0 || index >= children.size())
      throw new IndexOutOfBoundsException();

    if (index == selectedIndex)
      return; // no change
    
    // Disable last selected child element
    getSelectedChild().accept(ToggleFormVisitor.DISABLER);
    
    selectedIndex = index;
    
    // Enable new selected child element
    getSelectedChild().accept(ToggleFormVisitor.ENABLER);
    
    // Invoke listeners
    onSetSelectedIndex(index);
  }
  
  public void accept(FormVisitor visitor) {
    visitor.choice(this);
  }

  public <T> T apply(FormFunction<T> function) {
    return function.choice(this);
  }
  
  // Listeners

  public void addListener(ChoiceChangeListener listener) {
    listeners.add(listener);
  }

  public void removeListener(ChoiceChangeListener listener) {
    listeners.remove(listener);
  }
  
  public void onSetSelectedIndex(int index) {
    for (ChoiceChangeListener listener : listeners)
      listener.onSetSelectedIndex(index);
  }
  
  @Override
  public String toString() {
    return getFullSelector() + " choice";
  }

}
