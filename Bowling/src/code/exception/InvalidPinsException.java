package code.exception;

public class InvalidPinsException extends RuntimeException {
    public InvalidPinsException(String message) {
        super(message);
    }
}
