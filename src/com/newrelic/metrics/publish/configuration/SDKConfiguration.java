package com.newrelic.metrics.publish.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.newrelic.metrics.publish.binding.Context;



/**
 * An object representation of the data read from the {@code newrelic.properties} configuration file.
 */
public class SDKConfiguration {

	private static final String DEFAULT_LICENSE_KEY = "YOUR_LICENSE_KEY_HERE";
    private String licenseKey;
    private String serviceURI;
    private boolean sslHostVerification = true;
	private final String propertyFileName = "newrelic.properties";
	private final String configPath = "config";
	
	/**
     * Constructs a {@code SDKConfiguration}
     * @throws IOException if the {@code newrelic.properties} file does not exist
     * @throws ConfigurationException if there is an error reading the {@code newrelic.properties} file
     */
    public SDKConfiguration() throws IOException, ConfigurationException {        
        File file = getConfigurationFile();
        
        System.out.println("INFO: Using configuration file " + file.getAbsolutePath());
        
        if (!file.exists()) {
            throw logAndThrow(file.getAbsolutePath() + " does not exist");
        }
        
        Properties props = new Properties();
        InputStream inStream = new FileInputStream(file);
        try {
        	props.load(inStream);
        } finally {
        	inStream.close();
        }
        
        licenseKey = props.getProperty("licenseKey");
        if (null == licenseKey) {
            throw logAndThrow("licenseKey is undefined");
        }
        
        if(licenseKey.equals(DEFAULT_LICENSE_KEY)) {
            throw logAndThrow("You forgot to update the licenseKey from '" + DEFAULT_LICENSE_KEY + "'");        	
        }
        
        //host is optional and for debugging
        if(props.containsKey("host")) {
        	serviceURI = props.getProperty("host");
        	Context.getLogger().info("Metric service URI: " + serviceURI);
        }
        
        if (props.containsKey("sslHostVerification")) {
            sslHostVerification = Boolean.parseBoolean(props.getProperty("sslHostVerification"));
            Context.getLogger().fine("Using SSL host verification: " + sslHostVerification);
        }
    }

    /**
     * Get the license key
     * @return String
     */
    public String getLicenseKey() {
        return licenseKey;
    }

    /**
     * Set the license key
     * @param key
     */
    public void setLicenseKey(String key) {
        licenseKey = key;
    }

    /**
     * Get the poll interval
     * @return int the poll interval
     */
    public int getPollInterval() {
        return 60;
    }
    
    /**
     * For debug purposes only, not for general usage by clients of the SDK
     */
	public String internalGetServiceURI() {
		return serviceURI;
	}
	
	/**
	 * Returns if ssl host verification is enabled. 
	 * Adding {@code sslHostVerification} to {@code newrelic.properties}. It is {@code true} by default.
	 * @return boolean
	 */
	public boolean isSSLHostVerificationEnabled() {
	    return sslHostVerification;
	}
    
    private File getConfigurationFile() throws ConfigurationException {
//		TODO system property for config path
//      String path = System.getProperty("com.newrelic.platform.config");        
        String path = configPath + File.separatorChar + propertyFileName;
        File file = new File(path);
        if (!file.exists()) {
        	throw logAndThrow("Cannot find config file " + path);
        }
        return file;
	}
    
    private ConfigurationException logAndThrow(String message) {
    	Context.getLogger().severe(message);
        return new ConfigurationException(message);
    }
}
