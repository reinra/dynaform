package org.dynaform.xml.form.data;

import java.math.BigInteger;


public class IntegerData implements Data<BigInteger> {

	private static final long serialVersionUID = 1L;
	
	private BigInteger value;
	
	public Class<BigInteger> getType() {
		return BigInteger.class;
	}
	
	public BigInteger getValue() {
		return value;
	}
	
	public void setValue(BigInteger value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : new BigInteger(value);
	}

}
