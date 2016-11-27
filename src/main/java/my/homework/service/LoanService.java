package my.homework.service;

import my.homework.LoanApplicationRequest;

import javax.annotation.Nullable;
import java.util.List;

public interface LoanService {

    void apply(LoanApplicationRequest loanApplicationRequest, String countryCode, String requestUid);

    List<Loan> getAllLoansApproved(@Nullable Long personalId);
}
