package info.novatec.metricCollector.twitter;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

import info.novatec.metricCollector.commons.DateTimeConverter;


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
    SortedMap<Long, Integer> likesOfMentions; //datetime in seconds and likes
    Integer followers;

    DateTimeConverter dateTimeConverter;

    @Autowired
    public TwitterMetrics(DateTimeConverter dateTimeConverter){
        this.dateTimeConverter = dateTimeConverter;
    }

    void setLikesOfMentions(Map<String, Integer> likesOfMentions) {
        SortedMap<Long, Integer> sortedLikesOfMentions = new TreeMap<>();

        likesOfMentions.entrySet().forEach( likes -> {
            long dateTimeInSeconds = dateTimeConverter.twitterToSeconds(likes.getKey());
            sortedLikesOfMentions.put(dateTimeInSeconds, likes.getValue());
        });
        this.likesOfMentions = sortedLikesOfMentions;
    }
}
