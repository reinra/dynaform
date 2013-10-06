package org.dynaform.xml.form;

import org.dynaform.xml.form.layout.SectionStyle;

/**
 * Form section.
 * 
 * @author Rein Raudjärv
 * 
 * @see SectionStyle
 */
public interface FormSection<F extends Form> extends Form, Labeled {

	String getLabel();
	void setLabel(String label);
  
  SectionStyle getSectionStyle();
  void setSectionStyle(SectionStyle orientation);
	
	F getContent();
	
	// Listeners
  
  void addListener(SectionChangeListener<F> listener);
  void removeListener(SectionChangeListener<F> listener);
  
  public interface SectionChangeListener<F extends Form> {
    void onSetLabel(FormSection<F> section);
    void onSetSectionStyle(FormSection<F> section);
  }
	
}
