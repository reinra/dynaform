package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.Form;

/**
 * @author Rein Raudjärv
 * 
 * @see FormElementImpl
 * @see FormSectionImpl
 * @see FormRepeatImpl
 * @see FormSequenceImpl
 * @see FormChoiceImpl
 * @see FormAllImpl
 */
public abstract class BaseForm implements Form {
  
  private Form parent;
  private int counter;

  public Form getParent() {
    return parent;
  }

  public void _setParent(Form parent) {
    this.parent = parent;
  }
  
  public String getDescendantOrSelfSelector() {
    String id = getHighLevelId();
    return id == null ? null : HL_DESCENDANT_OR_SELF + id;
  }
  
  public String getHighLevelFullSelectorForChildren() {
    String parentId = parent == null ? "" : parent.getHighLevelFullSelectorForChildren();
    String highLevelId = getHighLevelId();
    
    String result = parentId;
    if (highLevelId != null)
      result += HL_CHILD + highLevelId;
    return result;
  }
  
  public String getFullSelector() {
    String parentId = parent == null ? "" : parent.getHighLevelFullSelectorForChildren();
    String highLevelId = getHighLevelId();
    String lowLevelId = getLowLevelId();
    
    String result = parentId;
    if (lowLevelId != null)
      result += LL_CHILD + lowLevelId;
    else if (highLevelId != null)
      result += HL_CHILD + highLevelId;
    return result;
  }
  
  public String nextCounterValue() {
    return String.valueOf(counter++);
  }

}
