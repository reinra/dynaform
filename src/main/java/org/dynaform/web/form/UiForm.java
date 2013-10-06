package org.dynaform.web.form;

import org.araneaframework.Widget;

/**
 * @author Rein Raudjärv
 */
public interface UiForm extends Widget {

  /**
   * Propagates the data to the general Form and validates it.
   * 
   * @return whether the data is valid.
   */
  boolean saveAndValidate();
  
}
