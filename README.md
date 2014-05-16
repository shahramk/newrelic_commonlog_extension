Apache CommonLog Format Reader
==========================================================
- - -
The Apache Common Log Format Reader for New Relic reads log files that use the CommonLogFormat of Apache, and reports all fields to New Relic as metrics.


##Prerequisites

*    A New Relic account. If you are not already a New Relic user, you can signup for a free account at http://newrelic.com
*    Obtain the New Relic Generic Log Reader plugin
*    A server that generates log files with apache common log format.
*    A configured Java Runtime (JRE) environment Version 1.7.
*    Network access to New Relic


##Additional Plugin Details:

*	This plugin reads each field in log file in compliance with Apache CommonLog Format, and it reports them to New Relic.
*	If you append data to existing log file, the previous set of data will be reported multiple times. So make sure you overwrite the log file for each cycle.


##Installation

Linux example:

*    $ mkdir /path/to/newrelic-plugin
*    $ cd /path/to/newrelic-plugin
*    $ tar xfz newrelic-*-plugin-*.tar.gz
   


## Configure the agent environment
New Relic plugins run an agent processes to collect and report metrics to New Relic. In order for that you need to configure your New Relic license and plugin-specific properties. Additionally you can set the logging properties.


### Configure your New Relic license
Specify your license key in a file by the name 'newrelic.properties' in the 'config' directory.
Your license key can be found under "Account Settings" at https://rpm.newrelic.com. See https://newrelic.com/docs/subscriptions/license-key for more help.

Linux example:

*    $ cp config/template_newrelic.properties config/newrelic.properties
*    $ Edit config/newrelic.properties and paste in your license key

### Configure plugin properties
Each plugin agent requires a JSON configuration file defining the access to the monitored log files. An example file is provided in the config directory.

Edit config/logfile.instance.json and specify the necessary property values. Change the values for "name", "host", "logfilename", "logfileformat", and "metricsofinterest". The value of the name field will appear in the New Relic user interface for the log file reader instance (i.e. "My Local Apache Access Log"). 

    [
      {
      "name"              : "My Local Apache Access Log",
      "host"              : "localhost",
      "logfilename"       : "/opt/apps/tomcat/logs/apache.log",
      "logfileformat"     : "%h %l %u %t \"%r\" %s %b",
      "metricsofinterest" : "%h %l %u %t \"%r\" %s %b"
       }
    ]

  * name              - A friendly name that will show up in the New Relic Dashboard.
  * host              - Hostname of the server being monitored.
  * logfilename       - log file name that is being monitored.
  * logfileformat     - apache commonlog format string as it appears in the log file.
  * metricsofinterest - fields that you'd like to generate metric for.

**Note:** Specify the above set of properties for each plugin instance. You will have to follow the syntax (embed the properties for each instance of the plugin in a pair of curley braces separated by a comma).

**Note:** If you would like to monitor multiple log files, copy the block of JSON properties (separated by comma), and change the values accordingly. Example:

    [
      {
      "name"              : "My Local Apache Access Log",
      "host"              : "localhost",
      "logfilename"       : "/opt/apps/tomcat/logs/apache.log",
      "logfileformat"     : "%h %l %u %t \"%r\" %s %b",
      "metricsofinterest" : "%h %l %u %t \"%r\" %s %b"
      },
      {
      "name"              : "My Other Apache Access Log",
      "host"              : "localhost",
      "logfilename"       : "/opt/apps/tomcat8/logs/apache.log",
      "logfileformat"     : "%h %l %u %t \"%r\" %s %b",
      "metricsofinterest" : "%h %u %t \"%r\" %b"
      }
    ]


### Configure logging properties
The plugin checks for the logging properties in config/logging.properties file. You can copy example_logging.properties and edit it if needed. By default he properties in this file are configured to log data at 'info' level to th console. You can edit the file and enable file logging.

Linux example:

*    $ cp config/example_loging.properties config/logging.properties


## Running the agent
To run the plugin in from the command line: 

*    `$ java -jar newrelic_*_plugin-*.jar`

If your host needs a proxy server to access the Internet, you can specify a proxy server & port: 

*    `$ java -Dhttps.proxyHost="PROXY_HOST" -Dhttps.proxyPort="PROXY_PORT" -jar newrelic_*_plugin-*.jar`

To run the plugin from the command line and detach the process so it will run in the background:

*    `$ nohup java -jar newrelic_*_plugin-*.jar &`

**Note:** At present there are no [init.d](http://en.wikipedia.org/wiki/Init) scripts to start the this plugin at system startup. You can create your own script, or use one of the services below to manage the process and keep it running:

*    [Upstart](http://upstart.ubuntu.com/)
*    [Systemd](http://www.freedesktop.org/wiki/Software/systemd/)
*    [Runit](http://smarden.org/runit/)
*    [Monit](http://mmonit.com/monit/)

## For support
Plugin support for troubleshooting assistance can be obtained by visiting New Relic support web site: (https://support.newrelic.com)
