package my.homework.dao;

import java.util.List;
import my.homework.LoanApplicationRequest;
import my.homework.service.Loan;
import my.homework.service.LoanApplicationStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public interface LoanApplicationDao {

    void addLoanApplication(
        long personalId,
        String name,
        String surname,
        String term,
        BigDecimal amount,
        LoanApplicationStatus status,
        String requestUid
    );

    List<Loan> getAllLoansApproved();
}
