package my.homework.service;

import my.homework.LoanApplicationRequest;
import my.homework.dao.EventDao;
import my.homework.dao.LoanApplicationDao;
import my.homework.exception.BlackListedPersonIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.Collection;
import java.util.UUID;

class LoanServiceImpl implements LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanApplicationDao loanApplicationDao;
    private final EventDao eventDao;
    private final BlackListService blackListService;

    LoanServiceImpl(LoanApplicationDao loanApplicationDao, EventDao eventDao, BlackListService blackListService) {
        this.blackListService = blackListService;
        this.loanApplicationDao = loanApplicationDao;
        this.eventDao = eventDao;
    }

    @Override
    @Async
    public void apply(LoanApplicationRequest loanApplicationRequest, UUID requestUid) {
        try {
            logger.debug(
                "starting loan application request processing; personalId: {}, requestUid: {}",
                loanApplicationRequest.getPersonId(),
                requestUid
            );

            blackListService.checkBlackListedPersonalId(loanApplicationRequest.getPersonId());

            loanApplicationDao.addLoanApplication(
                loanApplicationRequest.getPersonId(),
                loanApplicationRequest.getName(),
                loanApplicationRequest.getSurname(),
                loanApplicationRequest.getTerm(),
                loanApplicationRequest.getAmount(),
                requestUid
            );

            eventDao.addLoanApplicationEvent(loanApplicationRequest.getPersonId(), requestUid, 0);

            logger.debug(
                "loan application request processing has been successfully finished; personalId: {}, requestUid: {}",
                loanApplicationRequest.getPersonId(),
                requestUid
            );
        } catch (BlackListedPersonIdException exception) {
            logger.error(
                "loan application request has failed due to blacklisted personalId; personalId: {}, requestUid: {}",
                loanApplicationRequest.getPersonId(),
                requestUid
            );
            eventDao.addLoanApplicationEvent(loanApplicationRequest.getPersonId(), requestUid, 1);
        }
    }

    @Override
    public Collection<Loan> getAllLoansApproved() {
        return null;
    }

    @Override
    public LoanApplicationStatus getLoanApplicationStatus(Long personalId) {
        return null;
    }
}
