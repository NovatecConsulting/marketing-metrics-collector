package info.novatec.metricscollector.github.exception;

public class UserDeniedException extends RuntimeException{

    public UserDeniedException(String message){
        super(message);
    }
}
