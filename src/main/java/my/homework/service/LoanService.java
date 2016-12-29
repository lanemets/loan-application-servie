package my.homework.service;

import java.util.List;
import javax.annotation.Nullable;
import my.homework.constant.LoanApplicationRequest;
import my.homework.exception.BlackListedPersonIdException;

public interface LoanService {

    void apply(LoanApplicationRequest loanApplicationRequest, String countryCode, String requestUid) throws BlackListedPersonIdException;

    List<LoanApplication> getAllLoansApproved(@Nullable Long personalId);

    LoanApplication getLoanApplicationByUid(String applicationUid);
}
