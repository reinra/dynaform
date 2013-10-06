package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormAll;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.ToggleFormVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Rein Raudjärv
 */
public class FormAllImpl extends BaseForm implements FormAll, FormAll.OnChangeListener {

  private String id;
  
  private final List<Form> children;
  private final boolean allChildrenRequired;
  private final int[] indexes;
  private int[] selectedIndexes;
  private int[] unselectedIndexes;
  
  private final Collection<OnChangeListener> listeners = new ArrayList<OnChangeListener>();
  
  public FormAllImpl(List<Form> children, boolean allChildrenRequired) {
    if (children == null || children.size() <= 1)
      throw new IllegalArgumentException("Children: " + children);

    this.children = Collections.unmodifiableList(children);
    this.allChildrenRequired = allChildrenRequired;
    
    this.indexes = getAllIndexes(children.size());
    this.selectedIndexes = indexes;
    this.unselectedIndexes = new int[0];
    
    // Register myself as parent of all the children
    for (Form child : children)
      child._setParent(this);
  }
  
  public void _setParent(Form parent) {
    super._setParent(parent);
    
    id = OPERATOR_ALL + INDEX_START + parent.nextCounterValue() + INDEX_END;
  }
  
  public String getHighLevelId() {
    return null;
  }
  
  public String getLowLevelId() {
    return id;
  }
  
  public boolean areAllChildrenRequired() {
    return allChildrenRequired;
  }
  
  public List<Form> getChildren() {
    return children;
  }
  
  public int[] getIndexes() {
    return indexes;
  }
  
  public synchronized int[] getSelectedIndexes() {
    return selectedIndexes;
  }
  
  public synchronized int[] getUnselectedIndexes() {
    return unselectedIndexes;
  }
  
  public synchronized List<Form> getSelectedChildren() {
    return get(children, getSelectedIndexes());
  }
  
  public synchronized List<Form> getUnselectedChildren() {
    return get(children, getUnselectedIndexes());
  }

  public synchronized void setSelectedIndexes(int[] indexes) {
    int[] unselectedIndexes = validateAndFindUnselectedIndexes(indexes);
    
    // Enable last unselected child elements
    for (Form child : getUnselectedChildren())
      child.accept(ToggleFormVisitor.ENABLER);
    
    this.selectedIndexes = indexes;
    this.unselectedIndexes = unselectedIndexes;
    
    // Disable new unselected child elements
    for (Form child : getUnselectedChildren())
      child.accept(ToggleFormVisitor.DISABLER);
    
    // Invoke listeners
    onSetSelectedIndexes(indexes);
  }

  private int[] validateAndFindUnselectedIndexes(int[] indexes) {
    int size = children.size();
    
    if (indexes.length > size)
      throw new IllegalArgumentException("Too many indexes provided");
    
    // Cannot read XML with this validation
//    if (areAllChildrenRequired() && selectedIndexes.length < size)
//      throw new IllegalArgumentException("Not enough selectedIndexes provided (" + selectedIndexes.length + " instead of " + size + ")");
    
    boolean[] selected = new boolean[size];
    for (int i = 0; i < indexes.length; i++) {
      int index = indexes[i];
      if (index < 0 || index >= size)
        throw new IndexOutOfBoundsException("" + index);
      if (selected[index])
        throw new IllegalArgumentException("Index " + index + " occured more than once");
      selected[index] = true;
    }
    
    int[] disabledIndexes = new int[size - indexes.length];
    int counter = 0;
    for (int i = 0; i < size; i++)
      if (!selected[i])
        disabledIndexes[counter++] = i;
    return disabledIndexes;
  }
  
  public void accept(FormVisitor visitor) {
    visitor.all(this);
  }
  public <T> T apply(FormFunction<T> function) {
    return function.all(this);
  }
  
  // Listeners
  
  public void addListener(OnChangeListener listener) {
    listeners.add(listener);
  }
  public void removeListener(OnChangeListener listener) {
    listeners.remove(listener);
  }
  
  public void onSetSelectedIndexes(int[] indexes) {
    for (FormAll.OnChangeListener listener : listeners)
      listener.onSetSelectedIndexes(indexes);
  }
  
  @Override
  public String toString() {
    return getFullSelector() + " all";
  }
  
  // Static helper methods
  
  private static int[] getAllIndexes(int size) {
    int[] result = new int[size];
    for (int i = 0; i < result.length; i++)
      result[i] = i;
    return result;
  }
  
  private static <E> List<E> get(List<E> list, int[] indexes) {
    List<E> result = new ArrayList<E>();
    for (int i = 0; i < indexes.length; i++) {
      int index = indexes[i];
      result.add(list.get(index));
    }
    return result;
  }
  
}
