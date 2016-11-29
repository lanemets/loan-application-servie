package my.homework.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.annotation.Nullable;
import my.homework.service.LoanApplication;

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
