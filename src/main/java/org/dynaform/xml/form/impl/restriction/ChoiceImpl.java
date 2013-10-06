package org.dynaform.xml.form.impl.restriction;

import org.dynaform.xml.form.restriction.Choice;

import java.io.Serializable;


public class ChoiceImpl<E> implements Choice<E>, Serializable {

  private static final long serialVersionUID = 1L;
  
  private String label;
  private E value;
  
  public ChoiceImpl(E value) {
    this(value == null ? null : value.toString(), value);
  }
  
  public ChoiceImpl(String label, E value) {
    this.label = label;
    this.value = value;
  }
  
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  public E getValue() {
    return value;
  }
  public void setValue(E value) {
    this.value = value;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ChoiceImpl other = (ChoiceImpl) obj;
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

}
