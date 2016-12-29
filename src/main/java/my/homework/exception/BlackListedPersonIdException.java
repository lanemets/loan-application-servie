package my.homework.exception;

public class BlackListedPersonIdException extends RuntimeException {

    public BlackListedPersonIdException() {
        super("person has been blacklisted");
    }
}
