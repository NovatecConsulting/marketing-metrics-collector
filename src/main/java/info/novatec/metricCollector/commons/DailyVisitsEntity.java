package info.novatec.metricCollector.commons;

public class DailyVisitsEntity {

    private String timestamp;

    private Integer totalVisits;

    private Integer uniqueVisits;

    public DailyVisitsEntity(String timestamp, Integer totalVisits, Integer uniqueVisits){
        this.timestamp = timestamp;
        this.totalVisits = totalVisits;
        this.uniqueVisits = uniqueVisits;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Integer getTotalVisits() {
        return totalVisits;
    }

    public Integer getUniqueVisits() {
        return uniqueVisits;
    }
}
