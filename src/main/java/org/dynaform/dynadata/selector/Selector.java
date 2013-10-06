package org.dynaform.dynadata.selector;

import org.dynaform.xml.form.Form;

public interface Selector {

  String getString();
  
  boolean applies(Form form);
  
}
