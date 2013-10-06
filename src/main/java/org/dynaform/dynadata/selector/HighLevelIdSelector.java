package org.dynaform.dynadata.selector;

import org.dynaform.xml.form.Form;

public class HighLevelIdSelector extends BaseSelector {

  private final String highLevelId;
  
  public HighLevelIdSelector(String highLevelId) {
    this.highLevelId = highLevelId;
  }
  
  public String getString() {
    return Form.HL_DESCENDANT_OR_SELF + highLevelId;
  }

  public boolean applies(Form form) {
    return highLevelId.equals(form.getHighLevelId());
  }
  
}
