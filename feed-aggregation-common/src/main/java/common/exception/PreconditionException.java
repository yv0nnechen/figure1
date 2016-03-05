package common.exception;

/**
 * Exception thrown when a precondition check has failed.
 * Created by Yvonne on 2016-03-05.
 */
public class PreconditionException extends RuntimeException {
    public PreconditionException() {
    }

    public PreconditionException(String message) {
        super(message);
    }

    public PreconditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreconditionException(Throwable cause) {
        super(cause);
    }

    public PreconditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
