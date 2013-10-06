package org.dynaform.xml.form.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class DateData implements Data<Date> {

	private static final long serialVersionUID = 1L;
	
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	private static final String TIME_PATTERN = "HH:mm:ss";
	private static final String DATE_TIME_PATTERN = DATE_PATTERN + "'T'" + TIME_PATTERN;
	
	private final String pattern;
	private Date value;
	
	public static class _Date extends DateData {
		public _Date() { super(DATE_PATTERN); }
	}
	
	public static class _Time extends DateData {
		public _Time() { super(TIME_PATTERN); }
	}
	
	public static class _DateTime extends DateData {
		public _DateTime() { super(DATE_TIME_PATTERN); }
	}
	
	private DateData(String pattern) {
		this.pattern = pattern;
	}
	
	public Class<Date> getType() {
		return Date.class;
	}
	
	public Date getValue() {
		return value;
	}
	
	public void setValue(Date value) {
		this.value = value;
	}

	public String getXmlValue() {
		return value == null ? null : newFormat().format(value);
	}

	public void setXmlValue(String value) {
		try {
      this.value = value == null ? null : newFormat().parse(value);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
	}
	
	private DateFormat newFormat() {
		return new SimpleDateFormat(pattern);
	}

}
