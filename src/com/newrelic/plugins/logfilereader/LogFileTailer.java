package com.newrelic.plugins.logfilereader;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class LogFileTailer extends Thread {

	private long sampleInterval = 5000;
	private File logfile;
	private boolean startAtBeginning = false;
	private boolean tailing = false;
	private Set<LogFileTailerListener> listeners = new HashSet<LogFileTailerListener>();

  	static Logger logger;

	public LogFileTailer(File logFile) {
		this.logfile = logFile;
	}

	public LogFileTailer(File logFile, long sampleInterval, boolean startAtBeginning) {
		this.logfile = logFile;
		this.sampleInterval = sampleInterval;
		this.startAtBeginning = startAtBeginning;
	}

	public void addLogFileTailerListener(LogFileTailerListener logTailer) {
		this.listeners.add(logTailer);
	}

	public void removeLogFileTailerListener(LogFileTailerListener logTailer) {
		this.listeners.remove(logTailer);
	}

	protected void fireNewLogFileLine(String line) {
		for (Iterator<LogFileTailerListener> i = this.listeners.iterator(); i.hasNext();) {
			LogFileTailerListener logTailer = (LogFileTailerListener) i.next();
			logTailer.newLogFileLine(line);
		}
	}

	public void stopTailing() {
		this.tailing = false;
	}

	@Override
	public void run() {
		long filePointer = 0;

		if (this.startAtBeginning)
			filePointer = 0;
		else
			filePointer = this.logfile.length();

		try {
			this.tailing = true;
			RandomAccessFile file = new RandomAccessFile(logfile, "r");
			while (this.tailing) {
				try {
					long fileLength = this.logfile.length();
					//if (filePointer != fileLength) System.out.println(new java.util.Date() + "\t>>> File Pointer: " + filePointer + "  --  File Length: " + fileLength);
					if (fileLength < filePointer) {
						file = new RandomAccessFile(logfile, "r");
						filePointer = 0;
					}

					if (fileLength > filePointer) {
						file.seek(filePointer);
						String line = file.readLine();
						while (line != null) {
							this.fireNewLogFileLine(line);
							line = file.readLine();
						}
						filePointer = file.getFilePointer();
					}

					sleep(this.sampleInterval);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
