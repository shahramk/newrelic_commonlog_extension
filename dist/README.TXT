Apache Access Log plugin for New Relic (EARLY EVALUATION)
==========================================================
- - -
The Apache Access Log plugin for New Relic opens a log file with apache common log format specified in config/logfile.instance.json, seeks the end of the file, and starts reporting the contents of each line as metrics to New Relic as they are logged.


##Requirements

*    A New Relic account. If you are not already a New Relic user, you can signup for a free account at http://newrelic.com
*    Obtain the New Relic Apache Access Log plugin
*    A server running Apache server which is configured to generate access log with apache common log format.
*    A configured Java Runtime (JRE) environment Version 1.7.
*    Network access to New Relic


##Installation

Linux example:

*    $ mkdir "/path/to/newrelic-logfile-plugin"
*    $ cd "/path/to/newrelic-logfile-plugin"
*    $ tar xfz newrelic_apache_logfile_extension*.tar.gz

*	This will extract a 'config' directory with 3 properties files, a README.TXT file, the LICENSE terms, and a jar file in the current directory.
*	Update config/newrelic.properties with your license key (obtained from your active new relic account's 'Account Settings' page).
*	Update config/logfile.instance.json with the required information.

###Sample logfile.instance.json file:

	[
	    {
			"name" : "Apache Access Log",
			"host" : "localhost",
			"logfilename" : "REPLACE_THIS_WITH_YOUR_APACHE_ACCESS_LOG_FILE",
			"logfileformat" : "%{ids}C %D %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\"",
			"metricsofinterest" : "%{ids}C %D %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\""
	    }
	]

**Note:** Assign a unique meaningful name.
**Note:** The "host" property is not used at this time.
**Note:** "logfilename" field in config/logfile.instance.json must be set to the log file name you would like to use. 
**Note:** "logfileformat" is the common log format string from your apache settings.
**Note:** You can use "metricsofinterest" to tell the plugin which fields you are interested in reporting.


##Execution

Linux example:

*    `$ java -jar newrelic_apache_logfile_extension*.jar`

----------------------------------------------------------------------------------------------------------------------------
