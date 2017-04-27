package info.novatec.metricscollector.twitter.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import info.novatec.metricscollector.twitter.TwitterMetricsResult;

@Setter
@Component
@AllArgsConstructor
public abstract class TwitterMetricAbstract {

    @Autowired
    Twitter twitter;

    @Getter
    TwitterMetricsResult metrics;

    public abstract void collect() throws TwitterException;

    List<Status> getAllTweets(Query query) throws TwitterException {
        List<Status> allTweets = new ArrayList<>();
        QueryResult queryResult = twitter.search(query);
        List<Status> result = queryResult.getTweets();
        while (result.size() > 0) {
            allTweets.addAll(result);
            query.setMaxId(result.get(result.size() - 1).getId() - 1);
            result = twitter.search(query).getTweets();
        }
        return allTweets;
    }

    public List<Status> getUserTimeLine(String atUserName, UserTimeLineFilter filter) throws TwitterException{
        List<Status> tweets = new ArrayList<>();
        Paging paging = new Paging(1, 200);
        int pageIndex = 1;

        List<Status> pagedResult;
        do {
            pagedResult = twitter.getUserTimeline(atUserName, paging);
            pagedResult.stream()
                .filter(tweet -> filter==null || filter.apply(tweet))
                .collect(Collectors.toCollection( () -> tweets));
            paging = new Paging(++pageIndex, 200);
        }while(pagedResult.size()==200);

        return tweets;
    }

    public List<Status> getUserTimeLine(String atUserName) throws TwitterException{
        return getUserTimeLine(atUserName, null);
    }

    @FunctionalInterface
    public interface UserTimeLineFilter {
        boolean apply(Status tweet);
    }
}
