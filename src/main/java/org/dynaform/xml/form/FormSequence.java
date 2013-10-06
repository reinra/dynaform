package org.dynaform.xml.form;

import java.util.List;

/**
 * Form sequence.
 * <p>
 * This is a composition of other forms with fixed order.
 * 
 * @author Rein Raudjärv
 * 
 * @see FormAll
 */
public interface FormSequence extends Form {
	
	/**
	 * @return all child elements in their original order. 
	 */
	List<Form> getChildren();
	
}
