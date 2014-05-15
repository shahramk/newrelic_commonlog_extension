package com.newrelic.plugins.logfilereader;

import com.newrelic.metrics.publish.binding.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author shahram
 */
public class LogParser {

    static final Logger logger = Context.getLogger();
        
    public static void parseLine(String line, String[] logFileFormat, Map<String, Map<String, Metric>> metricData, String metricsOfInterest) {

        String url = null;
        long value = 0;
        String token = null;
        String metricCategory = null;
        String format = null;
        
        Map<String, Metric> lineMetrics = new HashMap<String, Metric>();

        line += " ";
        String tmpLine = line;
// httpd.conf:LogFormat "%{ids}C %D %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\"" bluehouse 
        
            try {
                for(int idx=0; idx< logFileFormat.length; idx++) {
                    format = logFileFormat[idx];
                    if(format.startsWith("\"")) {
                        format = format.substring(1, format.length()-1); // stripp off double quotes from format code 
                        token = line.substring(1, line.indexOf('"', 2));
                        if (idx<logFileFormat.length - 1) 
                            line = line.substring(token.length() + 3);
                    }
                    else if (format.equals("%t")) {
                        token = line.substring(2, line.indexOf(']'));
                        if (idx<logFileFormat.length - 1)
                        	line = line.substring(token.length() + 4);
                    }
                    else {
                        token = line.substring(0, line.indexOf(' '));
                        if (idx<logFileFormat.length - 1)
                        	line = line.substring(token.length() + 1);
                    }
                    if (token == null || token.equals("-") || token.equals("")) continue; // do not report empty metrics
                    if (!metricsOfInterest.contains(format)) continue; // not interested in capturing the value of this field

                    switch(format.charAt(format.length()-1)) {
                        case '%':       // percent sign
                            break;

                        case 'a':       // Client IP address and port of the request
                            // metrics = getMetricCategory(metricData, "ClientAddress");
                            // metric = getMetric(metrics, token, "ClientAddress", "count", 0, 1);
                            holdMetricInfo(lineMetrics, token, "ClientAddress", "count", 1);
                            break;

                        case 'A':       // Local IP-address
                            holdMetricInfo(lineMetrics, token, "LocalAddress", "count", 1);
                            break;

                        case 'B':       // Size of response in bytes, excluding HTTP headers
                        case 'b':       // Size of response in bytes, excluding HTTP headers (CLF)
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, token, "ResponseBodySize", "byte", value);
                            break;

                        case 'C':       // cookie {VARNAME}
                            holdMetricInfo(lineMetrics, token, "CustomerAccount", "count", 1);
                            break;

                        case 'D':       // time in microseconds to serve the request
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, token, "ServerTime", "usec", value);
                            break;

                        case 'e':       // content of environment variable {VARNAME}
                        	// TODO - content of environment variable {VARNAME} %e
                            break;

                        case 'f':       // file name
                            holdMetricInfo(lineMetrics, token, "FileName", "count", 1);
                            break;

                        case 'h':       // remote hostname
                            holdMetricInfo(lineMetrics, token, "HostName", "count", 1);
                            break;

                        case 'H':       // request protocol
                            holdMetricInfo(lineMetrics, token, "RequestProtocol", "count", 1);
                            break;

                        case 'i':       // contents of {VARNAME}: header line(s) in the request - i.e. %{Referer}i , %{User-Agent}i
                            // User-Agent: "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)"
                            metricCategory = format.substring(format.indexOf("{")+1, format.indexOf("}"));
                            if (!metricCategory.isEmpty() && metricCategory != null) {
                                if (metricCategory.equals("User-Agent")) {
                                    token = token.substring(0, token.indexOf(" "));
                                }
                                holdMetricInfo(lineMetrics, token, metricCategory, "count", 1);
                             }
                            break;

                        case 'k':       // Number of keepalive requests
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, "KeepAlive", "KeepAlives", "count", value);
                            break;

                        case 'l':       // remote logname from identd
                            holdMetricInfo(lineMetrics, token, "Identity", "count", 1);
                           break;

                        case 'L':       // request log ID
                            holdMetricInfo(lineMetrics, token, "LoginId", "count", 1);
                            break;

                        case 'm':       // request method
                            holdMetricInfo(lineMetrics, token, "RequestMethod", "count", 1);
                            break;

                        case 'n':       // contents of note {VARNAME}
                        	// TODO - contents of note {VARNAME} %n
                            break;

                        case 'o':       // contents of {VARNAME}: header line(s) in the reply
                        	// TODO - contents of {VARNAME}: header line(s) in the reply %o
                        	
                            break;

                        case 'p':       // canonical port of the server or client
                            // TODO - handle all possible %p formats
                            break;

                        case 'P':       // process ID of the child that served the request
                            // TODO - handle all possible %P formats
                            break;

                        case 'q':       // query string
                            holdMetricInfo(lineMetrics, token, "QueryString", "count", 1);
                            break;

                        case 'r':       // first line of request
                            String requestType = token.substring(0, token.indexOf(" "));
                            holdMetricInfo(lineMetrics, requestType, "RequestType", "count", 1);
                            
                            if (token.contains("?"))
                                token = token.substring(token.indexOf(" ")+1, token.indexOf("?")); // capture w/o QueryString
                            else
                                token = token.substring(token.indexOf(" ")+1, token.lastIndexOf(" "));
                            holdMetricInfo(lineMetrics, token, "Url", "count", 1);
                            url = token;
                            break;

                        case 'R':       // handler generating the response
                        	// TODO - handler generating the response %R
                            break;

                        case 's':       // status. %>s for final status
                            // metricCategory = format.contains(">") ? "HttpResponseCode" : "RedirectResponseCode";
                            metricCategory = "HttpResponseCode"; // report all response codes as HtpResponseCode - SK 8/17/13
                            holdMetricInfo(lineMetrics, token, metricCategory, "count", 1);
                            break;

                        case 't':       // request timestamp - {format}
                        	// TODO - request timestamp - {format} %t
                            // no use for the timestamp at this point
                            // holdMetricInfo(lineMetrics, token, "RequestTimestamp", "count", 1);
                            break;

                        case 'T':       // time in seconds to serve the request
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, token, "RequestLatency", "sec", value);
                            break;

                        case 'u':       // remote user if the request was authenticated
                            holdMetricInfo(lineMetrics, token, "RemoteUser", "count", 1);
                            break;

                        case 'U':       // URL path requested without query string
                            holdMetricInfo(lineMetrics, token, "Url", "count", 1);
                            url = token;
                            break;

                        case 'v':       // canonical server name serving the request
                            holdMetricInfo(lineMetrics, token, "CanonicalServer", "count", 1);
                            break;

                        case 'V':       // server name according to the 'UseCanonicalName' setting
                            holdMetricInfo(lineMetrics, token, "Server", "count", 1);
                            break;

                        case 'X':       // Connection aborted before the response completed
                            holdMetricInfo(lineMetrics, token, "ConnectionAborted", "count", 1);
                           break;

                        case '+':       // Connection may be kept alive after the response is sent
                            holdMetricInfo(lineMetrics, token, "ConnectionKeptAliveAfterSent", "count", 1);
                            break;

                        case '-':       // Connection will be closed after the response is sent
                            holdMetricInfo(lineMetrics, token, "ConnectionClosed", "count", 1);
                            break;

                        case 'I':       // bytes received, including request and headers
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, token, "BytesReceived", "byte", value);
                            break;

                        case 'O':       // bytes sent, including headers
                            value = (token.equals("-") || token.equals("")) ? 0 : Long.parseLong(token);
                            holdMetricInfo(lineMetrics, token, "BytesSent", "byte", value);
                            break;

                        default:
                            break;
                    }
                }
            } catch(java.lang.StringIndexOutOfBoundsException e) {
                logger.fine("String out of bound: line: " + tmpLine + "\ntoken: >" + token + "<\nformat: >" + format + "<");
                e.printStackTrace();
            } catch(java.lang.NumberFormatException e) {
            	logger.fine("Number format exception: line: " + tmpLine + "\ntoken: " + token + "\nformat: " + format + "\nvalue: " + value);
                e.printStackTrace();
            } catch(java.lang.NullPointerException e) {
            	logger.fine("NullPointerException exception: line: " + tmpLine + "\ntoken: " + token + "\nformat: " + format);
                e.printStackTrace();
            }
            
            //System.out.println(">>> Line: " + lineMetrics.toString());
            insertLineMetrics(metricData, lineMetrics, url);
            lineMetrics.clear();
            
    }
    
    private static Map<String, Metric> getMetricCategory(Map<String, Map<String, Metric>> metricData, String categoryName) {

        Map<String, Metric> hm = metricData.get(categoryName);
        if (hm == null) {
            // @TODO - create the map for this category
            hm = new HashMap<String, Metric>();
            metricData.put(categoryName, hm);
        }

        return hm;

    }
    
    private static Metric getMetric(Map<String, Metric> metrics, String metricName, String metricCategory, String metricUnit, long metricValue) {
        
        Metric m = metrics.get(metricName);
        if (m == null) {
            m = new Metric(metricName, metricCategory, metricUnit, metricValue);
            metrics.put(metricName, m);
        }
        
        return m;
    }
    
    private static void holdMetricInfo(Map<String, Metric> lineMetrics, String metricName, String metricCategory, String metricUnit, long metricValue) {
        Metric m = new Metric(metricName, metricCategory, metricUnit, metricValue);
        lineMetrics.put(metricCategory, m);
    }
    
    private static void insertLineMetrics(Map<String, Map<String, Metric>> metricData, Map<String, Metric> lineMetrics, String url) {
        // insert line metrics from lineMetrics hash object and insert into metricData
        Map<String, Metric> metrics;
        Metric metric;
        
        String metricName = null;
        String metricCategory = null;
        String metricUnit = null;
        long metricValue = 0;
        try {
	        Set<Entry<String, Metric>> lineMetricsSet = lineMetrics.entrySet();
	        Iterator<Entry<String, Metric>> lineMetricsIterator=lineMetricsSet.iterator();
	        
	        while(lineMetricsIterator.hasNext()) {
	            Entry<String, Metric> m =lineMetricsIterator.next();
	
	            Metric lm = (Metric)m.getValue();
	            
	            metricName = lm.name;
	            metricCategory = lm.category;
	            metricUnit = lm.unit;
	            metricValue = lm.value;
	            if (lm.unit.equals("byte") || lm.unit.equals("sec") || lm.unit.equals("usec") ||
	                lm.category.equals("KeepAlives") || lm.category.startsWith("Connection")) { 
	                // set metric name to url
	                metricName = url;
	            }
	            else if (lm.category.contains("ResponseCode")) {
	                metricCategory += "/" + metricName.charAt(0) + "xx";
	                metricName = lm.name + "-" + HttpCode.code.get(metricName);
	            }
	            
	            metrics = getMetricCategory(metricData, lm.category);
	            metric = getMetric(metrics, metricName, metricCategory, metricUnit, 0); // lm.value, lm.count);
	            metric.value += metricValue;
	            
	            if (lm.category.contains("ResponseCode")) { // update 1xx, 2xx, 3xx, 4xx, 5xx series
	                String metricX = "" + metricName.charAt(0) + "xx";
	                metricX += "-" + HttpCode.code.get(metricX);
	                metric = getMetric(metrics, metricX, lm.category, metricUnit, 0);
	                metric.value += metricValue;
	            }
	            //System.out.println("LogParser: >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> metric: " + metricName + " --- metric category: " + metricCategory + " --- metric value: " + metricValue);
	        }
        } catch(java.util.ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }
}
