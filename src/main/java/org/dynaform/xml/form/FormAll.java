package org.dynaform.xml.form;

import java.util.List;

/**
 * Form all.
 * <p>
 * This is a composition of other forms with dynamic order.
 * 
 * @author Rein Raudjärv
 * 
 * @see FormSequence
 */
public interface FormAll extends Form {
  
  /**
   * @return all child elements in their original order. 
   */
  List<Form> getChildren();
  
  /**
   * @return the indexes of the all child elements in the original order.
   */
  int[] getIndexes();
  
  /**
   * Returns <code>true</code> if all child elements must be selected.
   * <p>
   * This is <code>true</code> if the <code>minOccurs</code> was <code>0</code>
   * and <code>false</code> it it was <code>1</code>.
   * 
   * @return <code>true</code> if all child elements must be selected.
   */
  boolean areAllChildrenRequired();
  
  /**
   * @return the currently selected child elements in their corresponding order.
   */
  List<Form> getSelectedChildren();
  
  /**
   * @return the indexes of the currently selected child elements
   * in the corresponding order.
   */
  int[] getSelectedIndexes();
  
  /**
   * @param indexes the indexes of the currently selected child elements
   * in the corresponding order.
   */
  void setSelectedIndexes(int[] indexes);

  /**
   * @return the currently not selected child elements in their original order.
   */
  List<Form> getUnselectedChildren();
  
  /**
   * @return the indexes of currently not selected child elements
   * in their original order.
   */
  int[] getUnselectedIndexes();
  
  // Listeners
  
  void addListener(OnChangeListener listener);
  void removeListener(OnChangeListener listener);
  
  interface OnChangeListener {
    void onSetSelectedIndexes(int[] indexes);
  }
  
}
