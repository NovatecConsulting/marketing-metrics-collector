package info.novatec.metricscollector.twitter;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class TwitterMetrics {

    String userName;
    String atUserName;
    Integer tweets;
    Integer reTweets;
    Integer mentions;
    Integer likes;
    Map<String, Integer> likesOfMentions;
    Integer followers;

}
