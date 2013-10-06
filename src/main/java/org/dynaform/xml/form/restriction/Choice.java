package org.dynaform.xml.form.restriction;

/**
 * @author Rein Raudjärv
 */
public interface Choice<E> {

  String getLabel();
  void setLabel(String label);
  
  E getValue();
  void setValue(E value);
  
}
