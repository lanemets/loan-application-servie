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
    private final BlackListService blackListService;

    LoanServiceImpl(LoanApplicationDao loanApplicationDao, BlackListService blackListService) {
        this.blackListService = blackListService;
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

        boolean isPersonalIdBlackListed = blackListService.isPersonalIdBlackListed(loanApplicationRequest.getPersonalId());
        LoanApplicationStatus status = isPersonalIdBlackListed ? LoanApplicationStatus.PERSON_BLACKLISTED : LoanApplicationStatus.OK;

        loanApplicationDao.addLoanApplication(
            loanApplicationRequest.getPersonalId(),
            loanApplicationRequest.getName(),
            loanApplicationRequest.getSurname(),
            loanApplicationRequest.getTerm(),
            loanApplicationRequest.getAmount(),
            status,
            countryCode,
            requestUid
        );

        logger.debug(
            "loan application request processing has been successfully finished; personalId: {}, requestUid: {}, status: {}",
            loanApplicationRequest.getPersonalId(),
            requestUid,
            status
        );
    }

    @Override
    @Transactional
    public List<Loan> getAllLoansApproved(@Nullable Long personalId) {
        return loanApplicationDao.getAllLoansApproved(personalId);
    }

    @Override
    public Loan getLoanApplicationByUid(String applicationUid) {
        return loanApplicationDao.getLoanApplicationByUid(applicationUid);
    }
}
