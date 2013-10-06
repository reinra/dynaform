package org.dynaform.xml.form.data;

import org.dynaform.xml.XmlFormBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Rein Raudjärv
 * 
 * @see XmlFormBuilder
 */
public class DataUtil {

	private static Map<String, Class<? extends Data>> getDataClasses() {
		Map<String, Class<? extends Data>> result = new HashMap<String, Class<? extends Data>>();
		
		result.put("string", 	StringData.class);
		
		result.put("integer", 	IntegerData.class);			// An integer value
		result.put("long", 		LongData.class);			// A signed 64-bit integer
		result.put("int", 		IntData.class);				// A signed 32-bit integer
		result.put("short", 	ShortData.class);				// A signed 16-bit integer
		result.put("byte", 		ByteData.class);				// A signed 8-bit integer
		
		result.put("decimal", 	DecimalData.class);			// A decimal value
		
		result.put("date", 		DateData._Date.class);		// Defines a date value
		result.put("time", 		DateData._Time.class);		// Defines a time value
		result.put("dateTime", 	DateData._DateTime.class);	// Defines a date and time value
		
		result.put("boolean", 	BooleanData.class);	   // A boolean value
		
		return result;
	}
	
	public static Data newData(String primitiveName) {
		Class<? extends Data> clazz = getDataClasses().get(primitiveName);
		if (clazz == null)
			return null;
		
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
