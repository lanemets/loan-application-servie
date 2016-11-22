package my.homework.controller;

import my.homework.LoanApplicationRequest;
import my.homework.constant.ErrorResult;
import my.homework.constant.ErrorTypes;
import my.homework.constant.LoanResult;
import my.homework.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

            return new LoanResult<String>(requestUid.toString(), null);
        } catch (Exception exception) {
            logger.error("unexpected error has occurred during application process; personalId: {}", loanApplicationRequest.getPersonId());
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
            return null;
        } catch (Exception exception) {
            return null;
        }
    }

}
