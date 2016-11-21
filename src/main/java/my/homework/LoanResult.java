package my.homework;


import java.util.Objects;

public class LoanResult<T> {

    private final T result;
    private final ErrorResult errorResult;

    public LoanResult(T result, ErrorResult errorResult) {
        this.result = result;
        this.errorResult = errorResult;
    }

    public T getResult() {
        return result;
    }

    public ErrorResult getErrorResult() {
        return errorResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoanResult<?> that = (LoanResult<?>) o;
        return Objects.equals(result, that.result) &&
            Objects.equals(errorResult, that.errorResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, errorResult);
    }
}
