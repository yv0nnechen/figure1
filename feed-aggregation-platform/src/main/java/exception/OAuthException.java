package exception;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class OAuthException extends Exception {

    public OAuthException() {
        super();
    }

    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }
}
