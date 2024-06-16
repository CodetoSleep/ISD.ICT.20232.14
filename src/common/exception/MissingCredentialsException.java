package common.exception;

public class MissingCredentialsException extends AimsException {
    public MissingCredentialsException() {
        super("ERROR: Missing credentials! Please provide both username and password.");
    }
}
