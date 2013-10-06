package org.dynaform.xml.form;

import org.dynaform.xml.Factory;
import org.dynaform.xml.form.label.HeaderFactory;
import org.dynaform.xml.form.layout.RepeatStyle;

import java.util.List;

/**
 * Form repetition.
 * <p>
 * This is a dynamic set of forms of same kind.
 * The minimum and maximum element counts are fixed.
 * The maximum number may be unbounded meaning there's no limit. 
 * 
 * @author Rein Raudjärv
 * 
 * @see RepeatStyle
 */
public interface FormRepeat<F extends Form> extends Form {

  Factory<F> getFactory();
  Integer getMin();
  Integer getMax();

  boolean isDisabled();
  void setDisabled(boolean disabled);

  RepeatStyle getRepeatStyle();
  void setRepeatStyle(RepeatStyle repeatStyle);

  void setHeaderFactory(HeaderFactory headerFactory);
  HeaderFactory getHeaderFactory();

  List<F> getChildren();

  F add();
  void remove(F child);
  void clear();

  // Listeners

  void addListener(RepeatChangeListener<F> listener);
  void removeListener(RepeatChangeListener<F> listener);

  public interface RepeatChangeListener<F extends Form> {
    void onAdd(F child);
    void onRemove(F child);
    void onSetDisabled(FormRepeat<F> sequence);
    void onSetRepeatStyle(FormRepeat<F> sequence);
  }

}
