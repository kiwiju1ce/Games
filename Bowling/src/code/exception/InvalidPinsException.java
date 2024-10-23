package code.exception;

public class InvalidPinsException extends RuntimeException {
    public InvalidPinsException() {
        super();
    }

    public InvalidPinsException(String message) {
        super(message);
    }
}
