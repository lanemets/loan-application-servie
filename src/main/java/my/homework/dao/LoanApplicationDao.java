package my.homework.dao;

import my.homework.LoanApplicationRequest;
import my.homework.service.Loan;
import my.homework.service.LoanApplicationStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public interface LoanApplicationDao {

    void addLoanApplication(LoanApplicationRequest loanApplicationRequest, UUID requestUid, LoanApplicationStatus status);

    Collection<Loan> getAllLoansApproved();
}
