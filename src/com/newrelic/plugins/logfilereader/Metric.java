package com.newrelic.plugins.logfilereader;

/**
 *
 * @author shahram
 */
public class Metric implements IMetric {
    public final String name;
    public String category;
    public String unit;
    public long value;

    public Metric(String name) {
      this.name = name;
    }
    
    public Metric(String name, String category, String unit, long value) {
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.value = value;
    }
}

