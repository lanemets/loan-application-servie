package my.homework.service;

import my.homework.LoanApplyingRequest;
import my.homework.dao.LoanDao;
import org.springframework.scheduling.annotation.Async;

class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao;

    LoanServiceImpl(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    @Override
    public void apply(LoanApplyingRequest loanApplyingRequest) {
        loanDao.addLoanApplication(
            loanApplyingRequest.getPersonId(),
            loanApplyingRequest.getName(),
            loanApplyingRequest.getSurname(),
            loanApplyingRequest.getTerm(),
            loanApplyingRequest.getAmount()
        );
    }

    @Override
    @Async
    public void tryApply(LoanApplyingRequest loanApplyingRequest) {

    }

    @Override
    public LoanApplicationStatus getLoanApplicationStatus(Long personalId) {
        return null;
    }
}
