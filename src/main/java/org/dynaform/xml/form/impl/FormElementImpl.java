package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormVisitor;
import org.dynaform.xml.form.control.Control;
import org.dynaform.xml.form.data.Data;
import org.dynaform.xml.form.impl.restriction.RestrictionsImpl;
import org.dynaform.xml.form.restriction.Restrictions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Form element.
 * 
 * @author Rein Raudjärv
 * @param <F> data type.
 */
public class FormElementImpl<F> extends BaseForm implements FormElement<F> {

	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final Data<F> data;
	private final Restrictions<F> restrictions = new RestrictionsImpl<F>();
	
	private final Collection<ElementChangeListener<F>> listeners = new ArrayList<ElementChangeListener<F>>();
	
	private String label;
	private Control control;
	private boolean readOnly;
	private boolean required;
	private boolean disabled;
	
	public FormElementImpl(String id, Control control, Data<F> data) {
	  this.id = id;
		this.label = id;
		this.control = control;
		this.data = data;
	}
	
	public String getHighLevelId() {
	  return id;
	}
	
	public String getLowLevelId() {
	  return null;
	}

  public Data<F> getData() {
    return data;
  }
  
  public Restrictions<F> getRestrictions() {
    return restrictions;
  }
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		onSetLabel();
	}
	
  public Control getControl() {
    return control;
  }

  public void setControl(Control control) {
    this.control = control;
    onSetControl();
  }

	public boolean isReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		onSetReadOnly();
	}

  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
    onSetRequired();
  }

  public boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    onSetDisabled();
  }

	public Class<F> getType() {
		return data.getType();
	}

	public F getValue() {
		return data.getValue();
	}

	public String getXmlValue() {
		return data.getXmlValue();
	}

	public void setValue(F value) {
		if (readOnly)
			throw new UnsupportedOperationException("read only");
		
		data.setValue(value);
		onSetValue();
	}

	public void setXmlValue(String value) {
		data.setXmlValue(value);
		onSetValue();
	}

	public void accept(FormVisitor visitor) {
		visitor.element(this);
	}

	public <T> T apply(FormFunction<T> function) {
		return function.element(this);
	}

  public void addListener(ElementChangeListener<F> listener) {
    listeners.add(listener);
  }

  public void removeListener(ElementChangeListener<F> listener) {
    listeners.remove(listener);
  }
  
  public void onSetValue() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetValue(this);
  }
  
  public void onSetLabel() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetLabel(this);
  }
  
  public void onSetControl() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetControl(this);
  }

  public void onSetReadOnly() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetReadOnly(this);
  }
  
  public void onSetRequired() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetRequired(this);
  }
  
  public void onSetDisabled() {
    for (ElementChangeListener<F> listener : listeners)
      listener.onSetDisabled(this);
  }
  
  @Override
  public String toString() {
    return getFullSelector() + " element";
  }

  
}
