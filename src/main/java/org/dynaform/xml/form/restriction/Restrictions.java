package org.dynaform.xml.form.restriction;

import java.util.List;

/**
 * @author Rein Raudjärv
 */
public interface Restrictions<E> {

  // Enumeration
  
  List<Choice<E>> getChoices();
  
  // Length
  
  Integer getLength();
  void setLength(Integer length);
  
  Integer getMinLength();
  void setMinLength(Integer minLength);

  Integer getMaxLength();
  void setMaxLength(Integer maxLength);

  // Number Range
  
  E getMinInclusive();
  void setMinInclusive(E minInclusive);
  
  E getMinExclusive();
  void setMinExclusive(E minExclusive);
  
  E getMaxInclusive();
  void setMaxInclusive(E maxInclusive);
  
  E getMaxExclusive();
  void setMaxExclusive(E maxExclusive);
  
}
