package my.homework.service;

import my.homework.LoanApplicationRequest;

import java.util.Collection;
import java.util.UUID;

public interface LoanService {

    void apply(LoanApplicationRequest loanApplicationRequest, UUID requestUid);

    Collection<Loan> getAllLoansApproved();
}
