package org.dynaform.xml.form.data;



public class IntData implements Data<Integer> {

	private static final long serialVersionUID = 1L;
	
	private Integer value;
	
	public Class<Integer> getType() {
		return Integer.class;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Integer.valueOf(value);
	}

}
