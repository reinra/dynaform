package org.dynaform.xml.form.data;



public class ShortData implements Data<Short> {

	private static final long serialVersionUID = 1L;
	
	private Short value;
	
	public Class<Short> getType() {
		return Short.class;
	}
	
	public Short getValue() {
		return value;
	}
	
	public void setValue(Short value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Short.valueOf(value);
	}

}
