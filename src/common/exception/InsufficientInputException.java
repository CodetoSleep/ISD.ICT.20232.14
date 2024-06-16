package common.exception;

public class InsufficientInputException extends AimsException {
    public InsufficientInputException() {
        super("ERROR: Insufficient input! Please provide both username and password.");
    }
}
