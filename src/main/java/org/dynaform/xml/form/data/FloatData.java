package org.dynaform.xml.form.data;



public class FloatData implements Data<Float> {

	private static final long serialVersionUID = 1L;
	
	private Float value;
	
	public Class<Float> getType() {
		return Float.class;
	}
	
	public Float getValue() {
		return value;
	}
	
	public void setValue(Float value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Float.valueOf(value);
	}

}
