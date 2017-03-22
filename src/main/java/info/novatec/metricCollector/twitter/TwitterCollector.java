package info.novatec.metricCollector.twitter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    public TwitterCollector(ConfigProperties properties) {
        this.properties = properties;
        twitter = initialize();
    }

    private Twitter initialize() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(properties.getTwitterConsumerKey())
            .setOAuthConsumerSecret(properties.getTwitterConsumerSecret())
            .setOAuthAccessToken(properties.getTwitterOAuthToken())
            .setOAuthAccessTokenSecret(properties.getTwitterOauthTokenSecret());
        return new TwitterFactory(cb.build()).getInstance();
    }

    public TwitterMetrics collect() throws Exception {
        TwitterMetrics metrics = new TwitterMetrics();

        collectTweets(metrics);
        collectReTweets(metrics);
        collectMentions(metrics);
        collectLikes(metrics);
        collectLikesOfMentions(metrics);
        collectFollowers(metrics);

        return metrics;
    }

    private void collectTweets(TwitterMetrics metrics) throws TwitterException {
        int tweets = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline("NT_AQE", paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            tweets += results.size();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline("NT_AQE", paging);
        }
        metrics.setTweets(tweets);
    }

    private void collectReTweets(TwitterMetrics metrics) throws TwitterException{

        int reTweets = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline("NT_AQE", paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            reTweets += results.stream().mapToInt(tweet -> tweet.getRetweetCount()).sum();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline("NT_AQE", paging);
        }
        metrics.setReTweets(reTweets);
    }

    private void collectMentions(TwitterMetrics metrics) throws TwitterException{
        Query query = new Query("@AQE");
        QueryResult result = twitter.search(query);
        System.out.println(result.getTweets().size());

        int mentions = twitter.getMentionsTimeline().size();
        metrics.setMentions(mentions);
    }

    private void collectLikes(TwitterMetrics metrics) throws TwitterException{
        int likes = 0;
        Paging paging = new Paging(1, 200);
        List<Status> results = twitter.getUserTimeline("NT_AQE", paging);
        for(int pageIndex=2; results.size()>0; pageIndex++){
            likes += results.stream().mapToInt(tweet -> tweet.getFavoriteCount()).sum();
            paging = new Paging(pageIndex, 200);
            results = twitter.getUserTimeline("NT_AQE", paging);
        }
        metrics.setLikes(likes);
    }

    private void collectLikesOfMentions(TwitterMetrics metrics) throws TwitterException{
        int likesOfMentions = twitter.getMentionsTimeline().stream().mapToInt(tweet -> tweet.getFavoriteCount()).sum();
        metrics.setLikesOfMentions(likesOfMentions);
    }

    private void collectFollowers(TwitterMetrics metrics) throws TwitterException {
        int followers = twitter.getFollowersIDs(-1).getIDs().length;
        metrics.setFollowers(followers);
    }

}
