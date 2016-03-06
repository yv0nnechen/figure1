package exception;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class FeedServiceException extends Exception {
    public FeedServiceException() {
    }

    public FeedServiceException(String message) {
        super(message);
    }

    public FeedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedServiceException(Throwable cause) {
        super(cause);
    }
}
