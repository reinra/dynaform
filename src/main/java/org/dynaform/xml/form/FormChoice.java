package org.dynaform.xml.form;

import java.util.List;

/**
 * Form choice.
 * <p>
 * This is a choice between fixed set of forms.
 * Only one choice can be active at the same time.
 * 
 * @author Rein Raudjärv
 */
public interface FormChoice extends Form {

  /**
   * @return all child elements in their original order. 
   */
  List<Form> getChildren();
  
  /**
   * @return the currently selected child element.
   */
  Form getSelectedChild();
  
  /**
   * @return the index of the currently selected child element.
   */
  int getSelectedIndex();
  
  /**
   * @param index the index of the currently selected child element.
   */
  void setSelectedIndex(int index);
  
  // Listeners
  
  void addListener(ChoiceChangeListener listener);
  void removeListener(ChoiceChangeListener listener);
  
  public interface ChoiceChangeListener {
    void onSetSelectedIndex(int index);
  }
  
}
