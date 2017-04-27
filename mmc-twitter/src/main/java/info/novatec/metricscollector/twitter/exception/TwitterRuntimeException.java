package info.novatec.metricscollector.twitter.exception;

public class TwitterRuntimeException extends RuntimeException{

    public TwitterRuntimeException(twitter4j.TwitterException e) {
        super(e.getMessage());
    }
}
