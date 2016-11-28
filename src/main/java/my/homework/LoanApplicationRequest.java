package my.homework;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanApplicationRequest {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Long personalId;

    private String term;
    private String name;
    private String surname;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }

    public LoanApplicationRequest() {
    }

    public LoanApplicationRequest(BigDecimal amount, Long personalId, String term, String name, String surname) {
        this.amount = amount;
        this.personalId = personalId;
        this.term = term;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoanApplicationRequest that = (LoanApplicationRequest) o;
        return com.google.common.base.Objects.equal(amount, that.amount) &&
            com.google.common.base.Objects.equal(personalId, that.personalId) &&
            com.google.common.base.Objects.equal(term, that.term) &&
            com.google.common.base.Objects.equal(name, that.name) &&
            com.google.common.base.Objects.equal(surname, that.surname);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(amount, personalId, term, name, surname);
    }

    @Override
    public String toString() {
        return "LoanApplicationRequest{" +
            "amount=" + amount +
            ", personalId=" + personalId +
            ", term='" + term + '\'' +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            '}';
    }
}
