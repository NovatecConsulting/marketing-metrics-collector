package info.novatec.metricCollector.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Ints;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import info.novatec.metricCollector.commons.ConfigProperties;


@Component
public class TwitterCollector {

    private ConfigProperties properties;

    private Twitter twitter;

    private TwitterMetrics metrics;

    @Autowired
    public TwitterCollector(ConfigProperties properties, TwitterMetrics metrics) {
        this.properties = properties;
        this.metrics = metrics;
        twitter = initialize();
    }


    private Twitter initialize() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(properties.getTwitterConsumerKey())
            .setOAuthConsumerSecret(properties.getTwitterConsumerSecret())
            .setOAuthAccessToken(properties.getTwitterOAuthToken())
            .setOAuthAccessTokenSecret(properties.getTwitterOauthTokenSecret())
        .setUser(properties.getTwitterUserName())
        .setPassword(properties.getTwitterPassword());
        return new TwitterFactory(cb.build()).getInstance();
    }

    TwitterMetrics collect(String userName, String atUserName) throws TwitterException {
        metrics.setUserName(userName);
        metrics.setAtUserName(atUserName);

        collectTweets(metrics, atUserName);
        collectReTweets(metrics, atUserName);
        collectMentions(metrics, userName, atUserName);
        collectLikes(metrics);
        collectLikesOfMentions(metrics, userName, atUserName);
        collectFollowers(metrics);

        return metrics;
    }

    /**
     * Returns the number or tweets. Tweets that have been retweeted by {#atUserName} will be ignored.
     */
    private void collectTweets(TwitterMetrics metrics, String atUserName) throws TwitterException {
        int tweets = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline(atUserName, paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            tweets += results.stream().filter(tweet -> !tweet.isRetweeted()).count();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline(atUserName, paging);
        }
        metrics.setTweets(tweets);
    }

    /**
     * Returns the number of retweets. Tweets that have been retweeted by {#atUserName} will be ignored.
     */
    private void collectReTweets(TwitterMetrics metrics, String atUserName) throws TwitterException{
        int reTweets = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline(atUserName, paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            reTweets += results.stream()
                //filter out tweets from other users
                .filter(tweet -> !tweet.isRetweeted())
                .mapToInt(Status::getRetweetCount).sum();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline(atUserName, paging);
        }
        metrics.setReTweets(reTweets);
    }

    private void collectMentions(TwitterMetrics metrics, String userName, String atUserName) throws TwitterException{
        Query query = new Query("@"+atUserName);
        QueryResult result = twitter.search(query);

        //filter out mentions from user itself
        long mentioned = result.getTweets().stream()
            .filter( tweet -> !tweet.getUser().getName().equals(userName))
            .count();
        metrics.setMentions(Ints.checkedCast(mentioned));
    }

    private void collectLikes(TwitterMetrics metrics) throws TwitterException{
        int likes = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline("NT_AQE", paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            likes += results.stream().mapToInt(Status::getFavoriteCount).sum();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline("NT_AQE", paging);
        }
        metrics.setLikes(likes);
    }

    private void collectLikesOfMentions(TwitterMetrics metrics, String userName, String atUserName) throws TwitterException{
        Query query = new Query("@"+atUserName);
        query.setCount(20);
        List<Status> allTweets = getAllTweets(query);

        //filter out mentions from user itself
        Map<String, Integer> likesOfMentions = allTweets.stream()
            .filter( tweet -> !tweet.getUser().getName().equals(userName) )
            .filter( tweet -> tweet.getRetweetedStatus() != null)
            .collect(Collectors.toMap(
                tweet -> tweet.getCreatedAt().toString(),
                tweet -> tweet.getRetweetedStatus().getFavoriteCount()
            ));

        metrics.setLikesOfMentions(likesOfMentions);
    }

    private void collectFollowers(TwitterMetrics metrics) throws TwitterException {
        int followers = twitter.getFollowersIDs(-1).getIDs().length;
        metrics.setFollowers(followers);
    }

    private List<Status> getAllTweets(Query query) throws TwitterException{
        List<Status> allTweets = new ArrayList<>();
        List<Status> tweetsFromLastResult = twitter.search(query).getTweets();
        while(tweetsFromLastResult.size()>0){
            allTweets.addAll(tweetsFromLastResult);
            query.setMaxId(tweetsFromLastResult.get(tweetsFromLastResult.size()-1).getId()-1);
            tweetsFromLastResult = twitter.search(query).getTweets();
        }
        return allTweets;
    }

}
