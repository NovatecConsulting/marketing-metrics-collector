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
    private String hostName;

    public GaConfigProperties(@Value("${analytics.viewId}") String viewId,
                              @Value("${analytics.hostName}") String hostName) {
        this.viewId = viewId;
        this.hostName = hostName;
    }
}
