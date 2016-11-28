package my.homework.dao;

import my.homework.service.LoanApplication;

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
        String countryCode,
        String requestUid
    );

    List<LoanApplication> getAllLoansApproved(@Nullable Long personalId);

    LoanApplication getLoanApplicationByUid(String applicationUid);
}
