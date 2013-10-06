package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormSection;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.layout.SectionStyle;

import java.util.ArrayList;
import java.util.Collection;

public class FormSectionImpl<F extends Form> extends BaseForm implements FormSection<F> {

  private final String id;
  private final F content;
  
  private final Collection<SectionChangeListener<F>> listeners = new ArrayList<SectionChangeListener<F>>();

  private String label;
  private SectionStyle sectionStyle = SectionStyle.DEFAULT;

  public FormSectionImpl(String id, F content) {
    this.id = id;
    this.label = id;
    content._setParent(this);
    this.content = content;
  }

  public String getHighLevelId() {
    return id;
  }
  
  public String getLowLevelId() {
    return null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
    onSetLabel();
  }
  
  public SectionStyle getSectionStyle() {
    return sectionStyle;
  }

  public void setSectionStyle(SectionStyle orientation) {
    this.sectionStyle = orientation;
    onSetSectionStyle();
  }
  
  public F getContent() {
    return content;
  }

  public void accept(FormVisitor visitor) {
    visitor.section(this);
  }

  public <T> T apply(FormFunction<T> function) {
    return function.section(this);
  }
  
  public void addListener(SectionChangeListener<F> listener) {
    listeners.add(listener);
  }

  public void removeListener(SectionChangeListener<F> listener) {
    listeners.remove(listener);
  }
  
  public void onSetLabel() {
    for (SectionChangeListener<F> listener : listeners)
      listener.onSetLabel(this);
  }
  
  public void onSetSectionStyle() {
    for (SectionChangeListener<F> listener : listeners)
      listener.onSetSectionStyle(this);
  }
  
  @Override
  public String toString() {
    return getFullSelector() + " section";
  }

}
