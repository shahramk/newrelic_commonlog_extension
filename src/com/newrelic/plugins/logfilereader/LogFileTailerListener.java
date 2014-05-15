package com.newrelic.plugins.logfilereader;

public interface LogFileTailerListener {
  public void newLogFileLine(String line);
}
