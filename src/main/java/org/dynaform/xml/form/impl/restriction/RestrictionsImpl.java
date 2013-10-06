package org.dynaform.xml.form.impl.restriction;

import org.dynaform.xml.form.restriction.Choice;
import org.dynaform.xml.form.restriction.Restrictions;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Rein Raudjärv
 */
public class RestrictionsImpl<E> implements Restrictions<E> {

  private final List<Choice<E>> choices = new ArrayList<Choice<E>>(); 
  
  private Integer length;
  private Integer minLength;
  private Integer maxLength;
  
  private E minInclusive;
  private E minExclusive;
  private E maxInclusive;
  private E maxExclusive;
  
  public List<Choice<E>> getChoices() {
    return choices;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getMinLength() {
    return minLength;
  }

  public void setMinLength(Integer minLength) {
    this.minLength = minLength;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public E getMinInclusive() {
    return minInclusive;
  }

  public void setMinInclusive(E minInclusive) {
    this.minInclusive = minInclusive;
  }

  public E getMinExclusive() {
    return minExclusive;
  }

  public void setMinExclusive(E minExclusive) {
    this.minExclusive = minExclusive;
  }

  public E getMaxInclusive() {
    return maxInclusive;
  }

  public void setMaxInclusive(E maxInclusive) {
    this.maxInclusive = maxInclusive;
  }

  public E getMaxExclusive() {
    return maxExclusive;
  }

  public void setMaxExclusive(E maxExclusive) {
    this.maxExclusive = maxExclusive;
  }

}
