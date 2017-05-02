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
public class Metrics {

    public Metrics(String atUserName, String userName) {
        this.atUserName = atUserName;
        this.userName = userName;
    }

    String atUserName;
    String userName;
    Integer tweets;
    Integer reTweets;
    Integer mentions;
    Integer likes;
    Map<String, Integer> likesOfMentions; //timestamp, likes of mentions
    Integer followers;

}
