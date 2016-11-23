package my.homework.service;

public enum LoanApplicationStatus {

    OK(0),
    PERSON_BLACKLISTED(10);

    private int status;

    LoanApplicationStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    @Override
    public String toString() {
        return "LoanApplicationStatus{" +
            "status=" + status +
            '}';
    }
}
