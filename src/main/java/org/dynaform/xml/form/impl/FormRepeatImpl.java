package org.dynaform.xml.form.impl;

import org.dynaform.xml.Factory;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormRepeat;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.label.HeaderFactory;
import org.dynaform.xml.form.layout.RepeatStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Rein Raudjärv
 * 
 * @see FormRepeat
 */
public class FormRepeatImpl<F extends Form> extends BaseForm implements FormRepeat<F> {

  private final List<F> children = new ArrayList<F>();

  private final Factory<F> factory;

  private final Integer min;
  private final Integer max;
  
  private boolean disabled;

  private final Collection<RepeatChangeListener<F>> listeners = new ArrayList<RepeatChangeListener<F>>();

  private HeaderFactory headerFactory;
  private String highLevelId;
  private String lowLevelId;
  
  private RepeatStyle repeatStyle = RepeatStyle.DEFAULT;

  public FormRepeatImpl(Factory<F> factory, Integer min, Integer max) {
    this.factory = factory;
    this.min = min;
    this.max = max;
    
    F tempChild = factory.create();
    tempChild._setParent(this);
    lowLevelId = tempChild.getLowLevelId();
    highLevelId = tempChild.getHighLevelId();
  }
  
  public String getHighLevelFullSelectorForChildren() {
    return getParent() == null ? "" : getParent().getHighLevelFullSelectorForChildren();  
  }
  
  public String getHighLevelId() {
    return highLevelId;
  }
  
  public String getLowLevelId() {
    return lowLevelId;
  }

  public Factory<F> getFactory() {
    return factory;
  }

  public Integer getMin() {
    return min;
  }

  public Integer getMax() {
    return max;
  }
  
  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    onSetDisabled();
  }

  public void setHeaderFactory(HeaderFactory headerFactory) {
    this.headerFactory = headerFactory;
  }

  public HeaderFactory getHeaderFactory() {
    return headerFactory;
  }
  
  public void setRepeatStyle(RepeatStyle repeatStyle) {
    this.repeatStyle = repeatStyle;
    onSetRepeatStyle();
  }
  
  public RepeatStyle getRepeatStyle() {
    return repeatStyle;
  }

  public List<F> getChildren() {
    return children;
  }

  public F add() {
    F child = factory.create();
    child._setParent(this);
    children.add(child);
    onAdd(child);
    return child;
  }

  public void remove(F child) {
    if (!children.remove(child))
      throw new IllegalArgumentException();

    onRemove(child);
  }

  public void clear() {
    for (F child : new ArrayList<F>(children))
      remove(child);
  }

  public void accept(FormVisitor visitor) {
    visitor.repeat(this);
  }

  public <T> T apply(FormFunction<T> function) {
    return function.repeat(this);
  }

  public void addListener(RepeatChangeListener<F> listener) {
    listeners.add(listener);
  }

  public void removeListener(RepeatChangeListener<F> listener) {
    listeners.remove(listener);
  }

  public void onAdd(F child) {
    for (RepeatChangeListener<F> listener : listeners)
      listener.onAdd(child);
  }

  public void onRemove(F child) {
    for (RepeatChangeListener<F> listener : listeners)
      listener.onRemove(child);
  }
  
  public void onSetDisabled() {
    for (RepeatChangeListener<F> listener : listeners)
      listener.onSetDisabled(this);
  }
  
  public void onSetRepeatStyle() {
    for (RepeatChangeListener<F> listener : listeners)
      listener.onSetRepeatStyle(this);
  }
  
  @Override
  public String toString() {
    return getFullSelector() + " repeat";
  }

}
