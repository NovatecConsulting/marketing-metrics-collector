package info.novatec.metricscollector.commons.exception;

public class UserDeniedException extends RuntimeException{

    public UserDeniedException(String message){
        super(message);
    }
}
