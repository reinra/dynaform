package org.dynaform.xml.form.data;




public class LongData implements Data<Long> {

	private static final long serialVersionUID = 1L;
	
	private Long value;
	
	public Class<Long> getType() {
		return Long.class;
	}
	
	public Long getValue() {
		return value;
	}
	
	public void setValue(Long value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Long.valueOf(value);
	}

}
