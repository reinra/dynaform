package org.dynaform.xml;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormRepeat;

import java.util.Map;

/**
 * @author Rein Raudjärv
 * 
 * @see FormRepeat
 */
public interface RepeatAdapter<F extends Form> {

  /**
   * @return mapping between form elements and XML form elements.
   */
  Map<F, XmlForm> getChildren();

}