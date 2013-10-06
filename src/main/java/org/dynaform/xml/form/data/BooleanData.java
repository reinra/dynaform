package org.dynaform.xml.form.data;




public class BooleanData implements Data<Boolean> {

	private static final long serialVersionUID = 1L;
	
	private Boolean value;
	
	public Class<Boolean> getType() {
		return Boolean.class;
	}

	public Boolean getValue() {
		return value;
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Boolean.valueOf(value);
	}

}
