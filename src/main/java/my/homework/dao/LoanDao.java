package my.homework.dao;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface LoanDao {

    void addLoanApplication(long personalId, String name, String surname, String term, BigDecimal amount);
}
