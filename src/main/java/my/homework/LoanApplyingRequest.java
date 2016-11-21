package my.homework;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

class LoanApplyingRequest {

	@NotNull
	private BigDecimal amount;
	@NotNull
	private Long personId;

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

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LoanApplyingRequest that = (LoanApplyingRequest) o;
		return Objects.equals(amount, that.amount) &&
			Objects.equals(term, that.term) &&
			Objects.equals(name, that.name) &&
			Objects.equals(surname, that.surname) &&
			Objects.equals(personId, that.personId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, term, name, surname, personId);
	}
}
