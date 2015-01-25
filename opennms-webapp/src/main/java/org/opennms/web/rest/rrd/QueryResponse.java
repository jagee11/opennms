package org.opennms.web.rest.rrd;

import javax.xml.bind.annotation.*;

import java.util.List;
import java.util.Map;

@XmlRootElement(name = "query-response")
public class QueryResponse {
    public static class Metric {
        private long timestamp;
        private Map<String, Double> values;

        public Metric() {
        }

        public Metric(final long timestamp,
                      final Map<String, Double> values) {
            this.timestamp = timestamp;
            this.values = values;
        }

        @XmlAttribute(name = "timestamp")
        public long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(final long timestamp) {
            this.timestamp = timestamp;
        }

        @XmlElement(name = "values")
        public Map<String, Double> getValues() {
            return this.values;
        }

        public void setValues(final Map<String, Double> values) {
            this.values = values;
        }
    }

    private long step;

    private long start;
    private long end;

    private List<Metric> metrics;

    @XmlAttribute(name = "step")
    public long getStep() {
        return this.step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    @XmlAttribute(name = "start")
    public long getStart() {
        return this.start;
    }

    public void setStart(final long start) {
        this.start = start;
    }

    @XmlAttribute(name = "end")
    public long getEnd() {
        return this.end;
    }

    public void setEnd(final long end) {
        this.end = end;
    }

    @XmlElement(name = "metrics")
    public List<Metric> getMetrics() {
        return this.metrics;
    }

    public void setMetrics(final List<Metric> metrics) {
        this.metrics = metrics;
    }

    @Override
    public boolean equals(Object obj)
    {
       if (obj == null)
       {
          return false;
       }
       if (getClass() != obj.getClass())
       {
          return false;
       }
       final QueryResponse other = (QueryResponse) obj;

       return   com.google.common.base.Objects.equal(this.step, other.step)
             && com.google.common.base.Objects.equal(this.start, other.start)
             && com.google.common.base.Objects.equal(this.end, other.end)
             && com.google.common.base.Objects.equal(this.metrics, other.metrics);
    }

    @Override
    public int hashCode()
    {
       return com.google.common.base.Objects.hashCode(
                 this.step, this.start, this.end, this.metrics);
    }

    @Override
    public String toString()
    {
       return com.google.common.base.Objects.toStringHelper(this)
                 .add("Step", this.step)
                 .add("Start", this.start)
                 .add("End", this.end)
                 .add("Metrics", this.metrics)
                 .toString();
    }
}
