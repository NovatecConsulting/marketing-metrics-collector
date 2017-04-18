package info.novatec.metricscollector.google;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class GaConfigProperties {
    private String viewId;
    private String startPeriod;
    private String endPeriod;
    private String hostName;

    public GaConfigProperties(@Value("${analytics.viewId}") String viewId,
                              @Value(("${analytics.startPeriod}")) String startPeriod,
                              @Value(("${analytics.endPeriod}")) String endPeriod,
                              @Value("${analytics.hostName}") String hostName) {
        this.viewId = viewId;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.hostName = hostName;
    }
}
