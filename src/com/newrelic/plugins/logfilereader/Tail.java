package com.newrelic.plugins.logfilereader;

import java.util.Map;
import java.io.File;

public class Tail implements LogFileTailerListener {

    Map<Object, HttpCode> httpCodes;
    private LogFileTailer tailer;
    String[] logFileFormat;
    Map<String, Map<String, Metric>> metricData;
    String metricsOfInterest;
    
    public Tail(String filename, String[] logFileFormat, Map<String, Map<String, Metric>> metricData, String metricsOfInterest) {
        this.logFileFormat = logFileFormat;
        this.metricData = metricData;
        this.metricsOfInterest = metricsOfInterest;
        tailer = new LogFileTailer(new File(filename), 5000, false);
        tailer.addLogFileTailerListener(this);
        tailer.start();
    }

    @Override
    public void newLogFileLine(String line) {

      synchronized (metricData) {
          LogParser.parseLine(line, logFileFormat, metricData, metricsOfInterest);
      }

    }
}
