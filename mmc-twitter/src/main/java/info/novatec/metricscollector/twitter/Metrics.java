package info.novatec.metricscollector.twitter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Component
@NoArgsConstructor
public class Metrics {

    private String atUserName;
    private String userName;

    Map<String, Integer> likesOfMentions; //timestamp, likes of mentions
    Map<String, Integer> metrics = new HashMap<>();

    public Metrics(String atUserName, String userName) {
        this.atUserName = atUserName;
        this.userName = userName;
    }

    public void addMetric(String metricName, Integer value) {
        metrics.put(metricName, value);
    }

}
