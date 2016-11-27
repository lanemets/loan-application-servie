package my.homework.service;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.util.UUID;

public class Loan {

    private String term;
    private BigDecimal amount;
    private String personalName;
    private String personalSurname;
    private Long personalId;
    private String requestUid;

    public Loan(String term, BigDecimal amount, String personalName, String personalSurname, Long personalId, String requestUid) {
        this.term = term;
        this.amount = amount;
        this.personalName = personalName;
        this.personalSurname = personalSurname;
        this.personalId = personalId;
        this.requestUid = requestUid;
    }

    public Loan() {
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

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
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
            Objects.equal(personalId, loan.personalId) &&
            Objects.equal(requestUid, loan.requestUid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(term, amount, personalName, personalSurname, personalId, requestUid);
    }
}
