package org.tiqwab.example.gradle.multi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class ApplicationProp {

	private static final String PATH_JDBCPROP = "application.properties";
	
	public static Properties load() {
		Properties properties = new Properties();
		try (Reader reader = new InputStreamReader(ApplicationProp.class.getClassLoader().getResourceAsStream(PATH_JDBCPROP), "UTF-8")) {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
}
