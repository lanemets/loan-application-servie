package my.homework.constant;

import java.util.Objects;

public class ErrorResult {

    private String message;
    private ErrorType code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorType getCode() {
        return code;
    }

    public void setCode(ErrorType code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResult that = (ErrorResult) o;
        return Objects.equals(message, that.message) &&
            Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, code);
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
            "message='" + message + '\'' +
            ", code=" + code +
            '}';
    }
}
