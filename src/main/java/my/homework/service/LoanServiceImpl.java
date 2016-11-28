package my.homework.service;

import my.homework.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;

class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanApplicationDao loanApplicationDao;

    LoanServiceImpl(LoanApplicationDao loanApplicationDao) {
        this.loanApplicationDao = loanApplicationDao;
    }

    @Override
    @Async
    @Transactional
    public void apply(LoanApplicationRequest loanApplicationRequest, String countryCode, String requestUid) {
        logger.debug(
            "starting loan application request processing; personalId: {}, requestUid: {}",
            loanApplicationRequest.getPersonalId(),
            requestUid
        );

        loanApplicationDao.addLoanApplication(
            loanApplicationRequest.getPersonalId(),
            loanApplicationRequest.getName(),
            loanApplicationRequest.getSurname(),
            loanApplicationRequest.getTerm(),
            loanApplicationRequest.getAmount(),
            countryCode,
            requestUid
        );

        logger.debug(
            "loan application request processing has been successfully finished; personalId: {}, requestUid: {}",
            loanApplicationRequest.getPersonalId(),
            requestUid
        );
    }

    @Override
    @Transactional
    public List<LoanApplication> getAllLoansApproved(@Nullable Long personalId) {
        return loanApplicationDao.getAllLoansApproved(personalId);
    }

    @Override
    public LoanApplication getLoanApplicationByUid(String applicationUid) {
        return loanApplicationDao.getLoanApplicationByUid(applicationUid);
    }
}
