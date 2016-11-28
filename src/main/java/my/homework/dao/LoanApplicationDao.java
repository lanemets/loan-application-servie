package my.homework.dao;

import my.homework.service.LoanApplication;
import my.homework.service.LoanApplicationStatus;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public interface LoanApplicationDao {

    void addLoanApplication(
        long personalId,
        String name,
        String surname,
        String term,
        BigDecimal amount,
        LoanApplicationStatus status,
        String countryCode,
        String requestUid
    );

    List<LoanApplication> getAllLoansApproved(@Nullable Long personalId);

    LoanApplication getLoanApplicationByUid(String applicationUid);
}
