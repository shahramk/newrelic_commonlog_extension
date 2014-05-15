package com.newrelic.plugins.logfilereader.instance;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.binding.Context;
import com.newrelic.plugins.logfilereader.Tail;
import com.newrelic.plugins.logfilereader.HttpCode;
import com.newrelic.plugins.logfilereader.Metric;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author shahram
 */
public class LogFileReaderAgent extends Agent {
    
	private static final String GUID = "com.newrelic.plugins.logfilereader.instance";
	private static final String version = "0.0.5-beta";

	public static final String AGENT_DEFAULT_HOST = "localhost";		// Default values for Oracle Agent
	public static final String AGENT_DEFAULT_LOG = "logfile.log";

	public static final String AGENT_CONFIG_FILE = "logfile.instance.json";
    
    private static final String SEPARATOR = "/";

    public String[] logFileFormat;                               // apchae common log format string

    private String name;												// Agent Name

	private String host;												// Oracle Connection parameters
	private String logFileName;
	//private String metricsOfInterest;

  	static Logger logger;												// Local convenience variable

    long harvestCount = 0;

    public Map<Object, HttpCode> httpCodes = new HashMap<Object, HttpCode>();
    
    public Map<String, Map<String, Metric>> metricData = new HashMap<String, Map<String, Metric>>();
    
    
 	public LogFileReaderAgent(String name, String host, String logFileName, String logFileFormat, String metricsOfInterest) {
	   	super(GUID, version);

	   	this.name = name;												// Set local attributes for new class object
	   	this.host = host;
	   	this.logFileName = logFileName;
        this.logFileFormat = logFileFormat.split(" ");
        //this.metricsOfInterest = metricsOfInterest;

	   	logger = Context.getLogger();				    				// Set logging to current Context
	   	
	   	// Tail reads new lines at the tail of log file, and reports once a minute
        new Tail(this.logFileName, this.logFileFormat, metricData, metricsOfInterest);
	}
	
    @Override
	public void pollCycle() {
	 	logger.fine("Gathering Log File Reader Metrics. " + getAgentInfo());
	 	
	 	//report metrics gathered by Tail to new relic
        logger.info("harvest #: " + ++harvestCount);
		reportMetrics();											// Report Wait metrics to New Relic
		//logger.info("Reporting Log File Reader metrics: harvest cycle " + harvestCount + ".");
	}
    
    public void reportMetrics() {
        
        String unit;
        
        synchronized(metricData) {
	        Set<Entry<String, Map<String, Metric>>> s1 = metricData.entrySet();
	        
	        Iterator<Entry<String, Map<String, Metric>>> categoryIterator = s1.iterator();
	        
	        //System.out.println("\tCategories: " + s1.size());
	        
	        while (categoryIterator.hasNext()) {
	            Entry<String, Map<String, Metric>> category = categoryIterator.next();
	            //String categoryKey = (String)category.getKey();
	            //System.out.println("Category: " + categoryKey);
	            Map<String, Metric> categoryMap = (Map<String, Metric>)category.getValue();
	            
	            Set<Entry<String, Metric>> s2 = categoryMap.entrySet();
	            Iterator<Entry<String, Metric>> metricIterator=s2.iterator();
	            
	            //System.out.println("\t\tCategory: " + category.getKey() + "  --  size: " + categoryMap.size());
	
	            try {
		            while(metricIterator.hasNext()) {
		                //Entry<String, Metric> m = metricIterator.next(); 
		                //Metric metric=(Metric)m.getValue();
		                Metric metric = (Metric)metricIterator.next().getValue();
		                
		                if(metric.value > 0) {
		                    if (metric.unit.equals("byte"))
		                        unit = "Size";
		                    else if (metric.unit.contains("sec"))
		                        unit = "Latency";
		                    else
		                        unit = "Count";
		
		                    reportMetric(unit + SEPARATOR + metric.category + SEPARATOR + metric.name, metric.unit, metric.value);
		                    logger.fine("Metric " + unit + SEPARATOR + metric.category + SEPARATOR + metric.name + "   " + metric.unit + "=" + metric.value);      
		                }
		                
		                //if (metric.value > 0)
		                //    System.out.println("metric: " + metric.name + " --- metric category: " + metric.category + " ------- metric value: " + metric.value);
		                
		                metric.value = 0;
		            }
	            }catch(java.util.ConcurrentModificationException e) {
	                logger.fine("java.util.ConcurrentModificationException in com.newrelic.plugins.logfilereader.instance.LogFileReaderAgent.reportMetrics()");
	            }
	        }           
	        //System.out.println("--------------------------------------------------------------");
        }
    }
     
	@Override
	public String getComponentHumanLabel() {
		return name;
	}
    
	private String getAgentInfo() {
		return "Agent Name: " + name + ". Agent Version: " + version;
	}
}
