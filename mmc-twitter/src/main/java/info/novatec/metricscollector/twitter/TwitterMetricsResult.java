package info.novatec.metricscollector.twitter;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Component
@NoArgsConstructor
public class TwitterMetricsResult {

    public TwitterMetricsResult(String atUserName) {
        this.atUserName = atUserName;
    }

    String userName;
    String atUserName;
    Integer tweets;
    Integer reTweets;
    Integer mentions;
    Integer likes;
    Map<String, Integer> likesOfMentions;
    Integer followers;

}
