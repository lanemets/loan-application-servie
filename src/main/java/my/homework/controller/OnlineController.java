package my.homework.controller;

import my.homework.LoanApplicationRequest;
import my.homework.constant.ErrorResult;
import my.homework.constant.ErrorTypes;
import my.homework.constant.LoanResult;
import my.homework.service.Loan;
import my.homework.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
public class OnlineController {

    private static final Logger logger = LoggerFactory.getLogger(OnlineController.class);

    private final LoanService loanService;

    @Autowired
    public OnlineController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RequestMapping(
        value = "/apply",
        method = RequestMethod.PUT,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoanResult<?> apply(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        try {
            logger.debug("starting loan application request processing; personalId: {}", loanApplicationRequest.getPersonId());
            UUID requestUid = UUID.randomUUID();
            loanService.apply(loanApplicationRequest, requestUid);
            logger.debug(
                "loan application request processed successfully; personalId: {}, requestUid: {}",
                loanApplicationRequest.getPersonId(),
                requestUid
            );
            return new LoanResult<>(requestUid.toString(), null);
        } catch (Exception exception) {
            logger.error(String.format(
                "unexpected error has occurred during application process; personalId: %f",
                loanApplicationRequest.getPersonId()),
                exception
            );
            ErrorResult errorResult = new ErrorResult();
            errorResult.setCode(ErrorTypes.UNKNOWN);

            return new LoanResult<String>(null, errorResult);
        }
    }

    @RequestMapping(
        value = "/loans_approved",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoanResult<?> getAllLoansApproved() {
        try {
            logger.debug("starting all loans approved retrieving;");
            Collection<Loan> allLoansApproved = loanService.getAllLoansApproved();
            logger.debug("all loans approved retrieved successfully; loans retrieved count: {}", allLoansApproved.size());
            return new LoanResult<>(allLoansApproved, null);
        } catch (Exception exception) {
            logger.error("unexpected error has occurred during all approved loans retrieval;", exception);
            ErrorResult errorResult = new ErrorResult();
            errorResult.setCode(ErrorTypes.UNKNOWN);

            return new LoanResult<String>(null, errorResult);
        }
    }

}
