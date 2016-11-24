package my.homework.service;

import java.util.List;
import my.homework.LoanApplicationRequest;

import java.util.Collection;
import java.util.UUID;

public interface LoanService {

    void apply(LoanApplicationRequest loanApplicationRequest, String requestUid);

    List<Loan> getAllLoansApproved();
}
