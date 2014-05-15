package com.newrelic.plugins.logfilereader;

import java.util.HashMap;

/**
 *
 * @author shahram
 */
public class HttpCode {
    
    final static HashMap<String, String> code = new HashMap<String, String>();
    
    static {
       
       code.put("1xx", "Provisional");
       code.put("2xx", "Success");
       code.put("3xx", "Redirection");
       code.put("4xx", "Client Error");
       code.put("5xx", "Server Error");
       code.put("100", "Continue");
       code.put("101", "Switching Protocols");
       code.put("102", "Processing (WebDAV; RFC 2518)");
       code.put("200", "OK");
       code.put("201", "Created");
       code.put("202", "Accepted");
       code.put("203", "Non-Authoritative Information (since HTTP/1.1)");
       code.put("204", "No Content");
       code.put("205", "Reset Content");
       code.put("206", "Partial Content");
       code.put("207", "Multi-Status (WebDAV; RFC 4918)");
       code.put("208", "Already Reported (WebDAV; RFC 5842)");
       code.put("226", "IM Used (RFC 3229)");
       code.put("300", "Multiple Choices");
       code.put("301", "Moved Permanently");
       code.put("302", "Found");
       code.put("303", "See Other (since HTTP/1.1)");
       code.put("304", "Not Modified");
       code.put("305", "Use Proxy (since HTTP/1.1)");
       code.put("306", "Switch Proxy");
       code.put("307", "Temporary Redirect (since HTTP/1.1)");
       code.put("308", "Permanent Redirect (approved as experimental RFC)[12]");
       code.put("400", "Bad Request");
       code.put("401", "Unauthorized");
       code.put("402", "Payment Required");
       code.put("403", "Forbidden");
       code.put("404", "Not Found");
       code.put("405", "Method Not Allowed");
       code.put("406", "Not Acceptable");
       code.put("407", "Proxy Authentication Required");
       code.put("408", "Request Timeout");
       code.put("409", "Conflict");
       code.put("410", "Gone");
       code.put("411", "Length Required");
       code.put("412", "Precondition Failed");
       code.put("413", "Request Entity Too Large");
       code.put("414", "Request-URI Too Long");
       code.put("415", "Unsupported Media Type");
       code.put("416", "Requested Range Not Satisfiable");
       code.put("417", "Expectation Failed");
       code.put("418", "I'm a teapot (RFC 2324)");
       code.put("419", "Authentication Timeout");
       code.put("420", "Enhance Your Calm (Twitter)");
       code.put("422", "Unprocessable Entity (WebDAV; RFC 4918)");
       code.put("423", "Locked (WebDAV; RFC 4918)");
       code.put("424", "Failed Dependency (WebDAV; RFC 4918) / Method Failure (WebDAV)[14]");
       code.put("425", "Unordered Collection (Internet draft)");
       code.put("426", "Upgrade Required (RFC 2817)");
       code.put("428", "Precondition Required (RFC 6585)");
       code.put("429", "Too Many Requests (RFC 6585)");
       code.put("431", "Request Header Fields Too Large (RFC 6585)");
       code.put("444", "No Response (Nginx)");
       code.put("449", "Retry With (Microsoft)");
       code.put("450", "Blocked by Windows Parental Controls (Microsoft)");
       code.put("451", "Unavailable For Legal Reasons (Internet draft) / Redirect (Microsoft)");
       code.put("494", "Request Header Too Large (Nginx)");
       code.put("495", "Cert Error (Nginx)");
       code.put("496", "No Cert (Nginx)");
       code.put("497", "HTTP to HTTPS (Nginx)");
       code.put("499", "Client Closed Request (Nginx)");
       code.put("500", "Internal Server Error");
       code.put("501", "Not Implemented");
       code.put("502", "Bad Gateway");
       code.put("503", "Service Unavailable");
       code.put("504", "Gateway Timeout");
       code.put("505", "HTTP Version Not Supported");
       code.put("506", "Variant Also Negotiates (RFC 2295)");
       code.put("507", "Insufficient Storage (WebDAV; RFC 4918)");
       code.put("508", "Loop Detected (WebDAV; RFC 5842)");
       code.put("509", "Bandwidth Limit Exceeded (Apache bw/limited extension)");
       code.put("510", "Not Extended (RFC 2774)");
       code.put("511", "Network Authentication Required (RFC 6585)");
       code.put("598", "Network read timeout error (Unknown)");
       code.put("599", "Network connect timeout error (Unknown)");
    }  
}
