package org.dynaform.xml.form;


/**
 * A form visiting function. 
 * 
 * @author Rein Raudjärv
 * @param <T> the result type of each visit.
 * 
 * @see FormVisitor
 * @see Form
 */
public interface FormFunction<T> {
	
	<E> T element(FormElement<E> element);
	
	<F extends Form> T section(FormSection<F> section);
	
	<F extends Form> T repeat(FormRepeat<F> sequence);
	
	T sequence(FormSequence composite);
	
	T choice(FormChoice choice);
	
	T all(FormAll all);

}
