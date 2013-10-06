package org.dynaform.xml.form;

import org.dynaform.xml.form.control.Control;
import org.dynaform.xml.form.data.Data;
import org.dynaform.xml.form.restriction.Restrictions;



/**
 * Form input field.
 * 
 * @author Rein Raudjärv
 */
public interface FormElement<E> extends Form, Data<E>, Labeled {
	
  Data<E> getData();
  
  Restrictions<E> getRestrictions();
  
	String getLabel();
	void setLabel(String label);
	
	Control getControl();
	void setControl(Control control);
	
	boolean isReadOnly();
	void setReadOnly(boolean readOnly);
	
	boolean isRequired();
	void setRequired(boolean required);
	
	boolean isDisabled();
	void setDisabled(boolean disabled);
	
	// Listeners
	
  void addListener(ElementChangeListener<E> listener);
  void removeListener(ElementChangeListener<E> listener);
  
  public interface ElementChangeListener<E> {
    void onSetValue(FormElement<E> element);
    void onSetLabel(FormElement<E> element);
    void onSetControl(FormElement<E> element);
    void onSetReadOnly(FormElement<E> element);
    void onSetRequired(FormElement<E> element);
    void onSetDisabled(FormElement<E> element);
  }	
	
}
