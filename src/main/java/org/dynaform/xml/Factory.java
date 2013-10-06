package org.dynaform.xml;

/**
 * Factory.
 * 
 * @author Rein Raudjärv
 * @param <E> the result type.
 */
public interface Factory<E> {

	E create();
	
}
