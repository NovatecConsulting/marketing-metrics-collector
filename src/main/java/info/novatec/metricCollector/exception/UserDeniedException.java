package info.novatec.metricCollector.exception;

public class UserDeniedException extends RuntimeException{

    public UserDeniedException(String message){
        super(message);
    }
}
