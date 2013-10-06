package org.dynaform.xml;

/**
 * @author Rein Raudjärv
 */
public class MiscUtil {

	public static RuntimeException uncheck(Exception e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		return new RuntimeException(e);
	}
	
}
