package my.homework.constant;

public enum ErrorTypes implements ErrorType {

    UNKNOWN(-3000),
    BLACKLISTED(-3001);

    private int code;

    ErrorTypes(int code) {
        this.code = code;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ErrorTypes{" +
            "code=" + code +
            '}';
    }
}
