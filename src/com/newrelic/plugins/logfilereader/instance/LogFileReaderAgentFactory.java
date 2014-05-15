package com.newrelic.plugins.logfilereader.instance;

/**
 *
 * @author shahram
 */

import java.util.Map;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

public class LogFileReaderAgentFactory extends AgentFactory {

	public LogFileReaderAgentFactory() {
		super(LogFileReaderAgent.AGENT_CONFIG_FILE);
	}
	
	/**	
	 * Configure an agent based on an entry in the oracle json file.
	 * There may be multiple agents per Plugin - one per oracle instance
	 * 
	 */
	@Override
	public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
		String name = (String) properties.get("name");
		String host = (String) properties.get("host");
		String logFileName = (String) properties.get("logfilename");
		String logFileFormat = (String) properties.get("logfileformat");
		String metricsOfInterest = (String) properties.get("metricsofinterest"); // list of metrics of interest - if not in this list, it won't be reported

		/**
		 * Use pre-defined defaults to simplify configuration
		 */
		if (host == null || "".equals(host)) host = LogFileReaderAgent.AGENT_DEFAULT_HOST;
		if (logFileName == null || "".equals(logFileName)) logFileName = LogFileReaderAgent.AGENT_DEFAULT_LOG;
		
		if (logFileFormat == null || logFileFormat.isEmpty()) {
			System.err.println("\n\n\tPlease specify proper \"LogFileFormat\" in \"config/" + LogFileReaderAgent.AGENT_CONFIG_FILE + "\" and re-run the plugin\n");
			System.exit(1);
		}
     
		return new LogFileReaderAgent(name, host, logFileName, logFileFormat, metricsOfInterest);
	}
}
