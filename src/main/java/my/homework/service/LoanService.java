package my.homework.service;

import my.homework.LoanApplyingRequest;

public interface LoanService {

    void apply(LoanApplyingRequest loanApplyingRequest);

    void tryApply(LoanApplyingRequest loanApplyingRequest);

    LoanApplicationStatus getLoanApplicationStatus(Long personalId);
}
