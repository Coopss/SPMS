package com.spms.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConfigLoader {
	private static Properties prop = null;
	private static String fileName = "spms.config";
	private static InputStream is = null;
	private static HashMap<String, String> configMap = new HashMap<String, String>();
		
	// Load and populate configMap
	static {
		prop = new Properties();
		try {
		    is = new FileInputStream(fileName);
		} catch (FileNotFoundException ex) {
		    System.err.println("Could not find " + fileName);
		    System.exit(1);
		}
		
		try {
		    prop.load(is);
		} catch (IOException ex) {
			System.err.println("Failed to import properties from " + fileName);
			System.exit(1);
		}
		
		for (final String name: prop.stringPropertyNames())
			configMap.put(name, prop.getProperty(name));
		
	}

	public static String getProp(String key) {
		return configMap.get(key);
	}
	
}
