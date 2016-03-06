package exception;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class OAuthServiceException extends Exception {

    public OAuthServiceException() {
        super();
    }

    public OAuthServiceException(String message) {
        super(message);
    }

    public OAuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthServiceException(Throwable cause) {
        super(cause);
    }
}
