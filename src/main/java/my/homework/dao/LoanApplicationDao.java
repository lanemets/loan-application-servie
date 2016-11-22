package my.homework.dao;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

public interface LoanApplicationDao {

    void addLoanApplication(long personalId, String name, String surname, String term, BigDecimal amount, UUID requestUid);
}
