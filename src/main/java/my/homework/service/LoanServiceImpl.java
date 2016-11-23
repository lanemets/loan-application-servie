package my.homework.service;

import my.homework.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.UUID;

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
    public void apply(LoanApplicationRequest loanApplicationRequest, UUID requestUid) {
        logger.debug(
            "starting loan application request processing; personalId: {}, requestUid: {}",
            loanApplicationRequest.getPersonId(),
            requestUid
        );

        boolean isPersonalIdBlackListed = blackListService.isPersonalIdBlackListed(loanApplicationRequest.getPersonId());
        LoanApplicationStatus status = isPersonalIdBlackListed ? LoanApplicationStatus.PERSON_BLACKLISTED : LoanApplicationStatus.OK;

        loanApplicationDao.addLoanApplication(loanApplicationRequest, requestUid, status);

        logger.debug(
            "loan application request processing has been successfully finished; personalId: {}, requestUid: {}, status: {}",
            loanApplicationRequest.getPersonId(),
            requestUid,
            status
        );
    }

    @Override
    public Collection<Loan> getAllLoansApproved() {
        return loanApplicationDao.getAllLoansApproved();
    }
}
