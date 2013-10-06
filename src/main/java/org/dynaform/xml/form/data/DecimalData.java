package org.dynaform.xml.form.data;

import java.math.BigDecimal;


public class DecimalData implements Data<BigDecimal> {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal value;
	
	public Class<BigDecimal> getType() {
		return BigDecimal.class;
	}
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : new BigDecimal(value);
	}

}
