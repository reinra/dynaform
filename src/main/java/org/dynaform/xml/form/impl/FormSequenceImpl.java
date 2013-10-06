package org.dynaform.xml.form.impl;

import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormFunction;
import org.dynaform.xml.form.FormSequence;
import org.dynaform.xml.form.FormVisitor;

import java.util.Collections;
import java.util.List;


/**
 * Implementation of form composition.  
 * 
 * @author Rein Raudjärv
 * 
 * @see FormSequence
 */
public class FormSequenceImpl extends BaseForm implements FormSequence {
	
	public static Form newInstance(List<Form> forms) {
	  return newInstance(forms, false);
	}
	
	public static Form newInstance(List<Form> forms, boolean explicit) {
		if (forms == null || forms.isEmpty())
			return null;
		
		if (!explicit && forms.size() == 1)
		  return forms.get(0);
		
		return new FormSequenceImpl(forms, explicit); 
	}
	
	private final List<Form> children;
	private final boolean explicit;
	private String id;
	
	private FormSequenceImpl(List<Form> children, boolean explicit) {
	  for (Form child : children)
      child._setParent(this);
	  
		this.children = Collections.unmodifiableList(children);
		this.explicit = explicit;
	}
	
  public void _setParent(Form parent) {
    super._setParent(parent);
    
    if (explicit)
      id = OPERATOR_SEQUENCE + INDEX_START + parent.nextCounterValue() + INDEX_END;
  }

	public String getHighLevelId() {
	  return null;
	}
	
	public String getLowLevelId() {
	  return id;
	}
	
	public void add(Form child) {
	  child._setParent(this);
		children.add(child);
	}
	
	public List<Form> getChildren() {
		return children;
	}
	
	public void accept(FormVisitor visitor) {
		visitor.sequence(this);
	}

	public <T> T apply(FormFunction<T> function) {
		return function.sequence(this);
	}
	
  @Override
  public String toString() {
    return getFullSelector() + " sequence";
  }

}
