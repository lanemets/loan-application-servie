package my.homework.service;

import java.util.List;
import javax.annotation.Nullable;
import my.homework.constant.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import my.homework.exception.BlackListedPersonIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanApplicationDao loanApplicationDao;
    private final BlackListService blackListService;

    LoanServiceImpl(LoanApplicationDao loanApplicationDao, BlackListService blackListService) {
        this.loanApplicationDao = loanApplicationDao;
        this.blackListService = blackListService;
    }

    @Override
    @Async
    @Transactional
    public void apply(LoanApplicationRequest loanApplicationRequest, String countryCode, String requestUid) throws BlackListedPersonIdException {
        logger.debug(
            "starting loan application request processing; personalId: {}, requestUid: {}",
            loanApplicationRequest.getPersonalId(),
            requestUid
        );

        blackListService.checkBlackListed(loanApplicationRequest.getPersonalId());

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
    @Transactional(readOnly = true)
    public List<LoanApplication> getAllLoansApproved(@Nullable Long personalId) {
        return loanApplicationDao.getAllLoansApproved(personalId);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplication getLoanApplicationByUid(String applicationUid) {
        return loanApplicationDao.getLoanApplicationByUid(applicationUid);
    }
}
