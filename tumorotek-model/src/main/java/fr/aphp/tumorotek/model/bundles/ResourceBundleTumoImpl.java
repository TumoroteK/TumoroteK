package fr.aphp.tumorotek.model.bundles;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleTumoImpl implements ResourceBundleTumo {
	
	private Log log = LogFactory.getLog(ResourceBundleTumo.class);
	
	private String tumoPropertiesPath;

	public void setTumoPropertiesPath(String s) {
		this.tumoPropertiesPath = s;
	}

	@Override
	public boolean doesResourceBundleExists(String baseName) {
		boolean exist = true;

		if (baseName != null) {
			InputStreamReader reader = null;
			FileInputStream fis = null;
			File file = new File(tumoPropertiesPath, baseName);
			if (file.isFile()) { // Also checks for existance
				try {
					fis = new FileInputStream(file);
					reader = new InputStreamReader(fis, 
							Charset.forName("UTF-8"));
					reader.close();
					fis.close();
				} catch (FileNotFoundException e) {
					exist = false;
				} catch (IOException e) {
					log.error(e);
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						reader = null;
					}
					try {
						fis.close();
					} catch (IOException e) {
						fis = null;
					}
				}
			} else {
				exist = false;
			}
		} else {
			exist = false;
		}

		return exist;
	}

	@Override
	public ResourceBundle getResourceBundle(String baseName) {
		InputStreamReader reader = null;
		FileInputStream fis = null;
		ResourceBundle bundle = null;

		if (baseName != null) {
			File file = new File(tumoPropertiesPath, 
					baseName);
			if (file.isFile()) { // Also checks for existance
				try {
					fis = new FileInputStream(file);
					reader = new InputStreamReader(fis, 
							Charset.forName("UTF-8"));
					bundle = new PropertyResourceBundle(reader);
				} catch (FileNotFoundException e) {
					log.error(e);
				} catch (IOException e) {
					log.error(e);
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						reader = null;
					}
					try {
						fis.close();
					} catch (IOException e) {
						fis = null;
					}
				}
			}
		}

		return bundle;
	}
}
