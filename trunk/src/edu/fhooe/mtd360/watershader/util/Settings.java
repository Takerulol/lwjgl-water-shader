package edu.fhooe.mtd360.watershader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * loads and saves settings by given config file, if settings are missing, they will be written to the file.
 * it will also override comments in the settings file
 * 
 * @author TAKERU
 *
 */
public class Settings {
	
	public static final String WINDOW_WIDTH = "windowWidth";
	public static final String WINDOW_HEIGHT = "windowHeight";
	public static final String APPLICATION_NAME = "applicationName";
	
	public static final String defaultFileName = "settings.cfg";
	
	private static Settings instance = null;
	
	private Properties settings;
	private File file;
	
	private static final String SETTINGS_COMMENT = 
			"Water Shader Settings \n" +
			"#made by Christian Bollmann & Nils Juettemeier";
	
	private Settings() {
		
	}
	
	private void loadProperties() {
		if (this.file.exists() && !this.file.isDirectory()) {
			try {
				FileInputStream fis = new FileInputStream(this.file);
				this.settings.load(fis);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("file not found!");
		}
	}
		
		
	
	public static void saveProperties() {
		checkInstance();
		try {
			FileOutputStream fos = new FileOutputStream(new File(defaultFileName));
			Settings.instance.settings.store(fos, SETTINGS_COMMENT);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void checkInstance() {
		if (Settings.instance == null) {
			init();
		}
	}

	public static int getIntSetting(String key) {
		checkInstance();
		return Integer.parseInt(Settings.instance.settings.getProperty(key));
	}
	
	public static String getStringSetting(String key) {
		checkInstance();
		return Settings.instance.settings.getProperty(key);
	}

	public static Float getFloatSetting(String key) {
		checkInstance();
		return Float.parseFloat(Settings.instance.settings.getProperty(key));
	}
	
	/**
	 * just pre-initialization
	 */
	public static void init() {
		if (Settings.instance == null) {
			instance = new Settings();
			
			instance.file = new File(Settings.defaultFileName);
			
			instance.settings = new Properties(System.getProperties());
			

			initSettings();
			
			instance.loadProperties();
			
			instance.settings.list(System.out);
			
			saveProperties();
		}
	}
	
	private static void set(String key, String value) {
		instance.settings.setProperty(key, value);
	}

	
	/*  #############################################
	 *  #				DEFAULT SETTINGS			#
	 *  #############################################
	 */ 
	private static void initSettings() {
		initWindowSettings();
		initShaderSettings();
	}
	

	private static void initWindowSettings() {
		set("applicationName", "Water Shader - Christian Bollmann, Nils Juettemeier");
		set("windowWidth", Integer.toString(1024));
		set("windowHeight", Integer.toString(768));
	}
	
	private static void initShaderSettings() {
		
	}
	
}
