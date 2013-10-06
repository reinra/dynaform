package org.dynaform.xml.form;

/**
 * @author Rein Raudjärv
 * 
 * @see Form
 * @see FormFunction
 */
public interface FormVisitor {

	<E> void element(FormElement<E> element);
	
	<F extends Form> void section(FormSection<F> section);
	
	<F extends Form> void repeat(FormRepeat<F> sequence);
	
	void sequence(FormSequence composite);
	
	void choice(FormChoice choice);
	
	void all(FormAll all);
	
}
