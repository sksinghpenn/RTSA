package com.lifotech.rtsa.storm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This a singleton class. It provides value for the property from the property
 * file.
 * 
 * @author SK Singh
 * 
 */

public final class AppConfig {

	private static Properties config = null;

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	/**
	 * This is a private constructor to not allow client instantiate this class.
	 */
	private AppConfig() {

		config = new Properties();

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(AppConstant.APP_CONFIG_FILE_NAME);

		if (inputStream != null) {
			try {
				config.load(inputStream);
			} catch (IOException e) {
				logger.error("Could not load properties : ");
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Gets the property value for the given property name
	 * 
	 * @param propertyName
	 * @return String -- the property value
	 */
	public static String getPropertyValue(String propertyName) {
		if (config == null) {
			synchronized (AppConfig.class) {
				new AppConfig();
			}

		}

		return config.getProperty(propertyName);

	}
}
