package exception;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class PixelClientException extends Exception {
    public PixelClientException() {
    }

    public PixelClientException(String message) {
        super(message);
    }

    public PixelClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PixelClientException(Throwable cause) {
        super(cause);
    }
}
