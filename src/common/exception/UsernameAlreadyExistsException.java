package common.exception;

public class UsernameAlreadyExistsException extends AimsException {
    public UsernameAlreadyExistsException() {
        super("ERROR: Username already exists!");
    }
}
