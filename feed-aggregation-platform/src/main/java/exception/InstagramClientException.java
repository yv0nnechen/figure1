package exception;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class InstagramClientException extends Exception {

    public InstagramClientException() {
    }

    public InstagramClientException(String message) {
        super(message);
    }

    public InstagramClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstagramClientException(Throwable cause) {
        super(cause);
    }
}
