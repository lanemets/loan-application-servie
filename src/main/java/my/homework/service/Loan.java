package my.homework.service;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.UUID;

public class Loan {

    private String term;
    private BigDecimal amount;
    private String personalName;
    private String personalSurname;
    private String requestUid;

    public Loan(
        String term,
        BigDecimal amount,
        String personalName,
        String personalSurname,
        String requestUid
    ) {
        this.term = term;
        this.amount = amount;
        this.personalName = personalName;
        this.personalSurname = personalSurname;
        this.requestUid = requestUid;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getPersonalSurname() {
        return personalSurname;
    }

    public void setPersonalSurname(String personalSurname) {
        this.personalSurname = personalSurname;
    }

    public String getRequestUid() {
        return requestUid;
    }

    public void setRequestUid(String requestUid) {
        this.requestUid = requestUid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Loan loan = (Loan) o;
        return Objects.equal(term, loan.term) &&
            Objects.equal(amount, loan.amount) &&
            Objects.equal(personalName, loan.personalName) &&
            Objects.equal(personalSurname, loan.personalSurname) &&
            Objects.equal(requestUid, loan.requestUid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term, amount, personalName, personalSurname, requestUid);
    }

    @Override
    public String toString() {
        return "Loan{" +
            "term='" + term + '\'' +
            ", amount=" + amount +
            ", personalName='" + personalName + '\'' +
            ", personalSurname='" + personalSurname + '\'' +
            ", requestUid=" + requestUid +
            '}';
    }
}
