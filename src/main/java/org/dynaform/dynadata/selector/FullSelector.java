package org.dynaform.dynadata.selector;

import org.dynaform.xml.form.Form;

public class FullSelector extends BaseSelector {
  
  private final String fullSelector;
  
  public FullSelector(String fullSelector) {
    this.fullSelector = fullSelector;
  }
  
  public String getString() {
    return fullSelector;
  }

  public boolean applies(Form form) {
    return fullSelector.equals(form.getFullSelector());
  }
  
}
