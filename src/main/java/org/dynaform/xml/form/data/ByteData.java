package org.dynaform.xml.form.data;



public class ByteData implements Data<Byte> {

	private static final long serialVersionUID = 1L;
	
	private Byte value;
	
	public Class<Byte> getType() {
		return Byte.class;
	}
	
	public Byte getValue() {
		return value;
	}
	
	public void setValue(Byte value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : value.toString();
	}

	public void setXmlValue(String value) {
		this.value = value == null ? null : Byte.valueOf(value);
	}

}
