package my.homework.exception;

public class BlackListedPersonIdException extends RuntimeException {

    public BlackListedPersonIdException() {
        super("person is blacklisted");
    }
}
