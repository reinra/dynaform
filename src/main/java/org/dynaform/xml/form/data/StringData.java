package org.dynaform.xml.form.data;


public class StringData implements Data<String> {

	private static final long serialVersionUID = 1L;
	
	private String value;
	
	public Class<String> getType() {
		return String.class;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value;
	}

	public void setXmlValue(String value) {
		this.value = value;
	}

}
