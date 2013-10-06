package org.dynaform.dynadata.selector;

import org.dynaform.xml.form.Form;

public class SelectorFactory {

  public static Selector newInstance(String str) {
    if (str.startsWith(Form.HL_DESCENDANT_OR_SELF))
      return new HighLevelIdSelector(str.substring(Form.HL_DESCENDANT_OR_SELF.length()));
    
    if (str.startsWith(Form.HL_CHILD))
      return new FullSelector(str);
    
    throw new IllegalArgumentException(str);
  }
  
}
