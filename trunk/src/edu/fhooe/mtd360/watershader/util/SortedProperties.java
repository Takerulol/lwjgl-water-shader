package edu.fhooe.mtd360.watershader.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SortedProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5562321432056606335L;
	
	public SortedProperties() {
		super();
	}

	public SortedProperties(Properties defaults) {
		super(defaults);
	}

	/**
	 * Overrides, called by the store method.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized Enumeration keys() {
		Enumeration keysEnum = super.keys();
		Vector keyList = new Vector();
		while (keysEnum.hasMoreElements()) {
			keyList.add(keysEnum.nextElement());
		}
		Collections.sort(keyList);
		return keyList.elements();
	}

}
