package org.dynaform.xml.form.data;




public class DoubleData implements Data<Double> {

	private static final long serialVersionUID = 1L;
	
	private Double value;
	
	public Class<Double> getType() {
		return Double.class;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Double.valueOf(value);
	}

}
