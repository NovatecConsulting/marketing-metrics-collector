package info.novatec.metricCollector.twitter;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TwitterMetrics {

    Integer tweets;
    Integer reTweets;
    Integer mentions;
    Integer likes;
    Integer likesOfMentions;
    Integer followers;

}
