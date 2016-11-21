package my.homework;

public class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao;

    public LoanServiceImpl(LoanDao loanDao) {
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
}
