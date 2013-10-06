package org.dynaform.xml.form.data;

import java.io.Serializable;

/**
 * @author Rein Raudjärv
 */
public interface Data<E> extends Serializable {
	
	/**
	 * @return data type.
	 */
	Class<E> getType();
	
	/**
	 * @return current value (may be <code>null</code>).
	 */
	E getValue();
	
	/**
	 * @param value new value (may be <code>null</code>).
	 */
	void setValue(E value);
	
	/**
	 * @return XML value (may be <code>null</code>).
	 */
	String getXmlValue();
	
	/**
	 * @param value XML value (may be <code>null</code>).
	 */
	void setXmlValue(String value);
	
}
