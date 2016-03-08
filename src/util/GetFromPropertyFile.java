package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GetFromPropertyFile {
	
	public static final Logger LOG = Logger.getLogger(GetFromPropertyFile.class);
	private String dAOType = "";
	private String path = "";
	private InputStream inputStream;
	
	public String getPath() throws IOException {
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			path = prop.getProperty("path");
			
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			inputStream.close();
		}
		return path;
	}
	
	public String getDAOType() throws IOException {
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			dAOType = prop.getProperty("dAOType");
			
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			inputStream.close();
		}
		return dAOType;
	}
}
