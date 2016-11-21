package my.homework;

public enum ErrorTypes implements ErrorType {

    UNKNOWN(-32603);

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
