package exception;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class ClientException extends Exception {
    public ClientException() {
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
