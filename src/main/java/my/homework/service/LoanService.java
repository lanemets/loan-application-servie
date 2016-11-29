package my.homework.service;

import java.util.List;
import javax.annotation.Nullable;
import my.homework.LoanApplicationRequest;

public interface LoanService {

    void apply(LoanApplicationRequest loanApplicationRequest, String countryCode, String requestUid);

    List<LoanApplication> getAllLoansApproved(@Nullable Long personalId);

    LoanApplication getLoanApplicationByUid(String applicationUid);
}
