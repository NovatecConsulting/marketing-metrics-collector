package info.novatec.metricscollector.commons;

import lombok.Getter;


@Getter
public class DailyVisitsEntity {

    private String timestamp;

    private Integer totalVisits;

    private Integer uniqueVisits;

    public DailyVisitsEntity(String timestamp, Integer totalVisits, Integer uniqueVisits) {
        this.timestamp = timestamp;
        this.totalVisits = totalVisits;
        this.uniqueVisits = uniqueVisits;
    }

}
