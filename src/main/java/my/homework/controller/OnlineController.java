package my.homework.controller;

import my.homework.LoanApplyingRequest;
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
    public LoanResult<?> apply(@RequestBody LoanApplyingRequest loanApplyingRequest) {
        try {
            logger.debug("starting loan application request processing; personalId: {}", loanApplyingRequest.getPersonId());
            loanService.apply(loanApplyingRequest);

            return new LoanResult<>("OK", null);
        } catch (Exception exception) {
            logger.debug("unexpected error has occurred during application process; personalId: {}", loanApplyingRequest.getPersonId());
            ErrorResult errorResult = new ErrorResult();
            errorResult.setCode(ErrorTypes.UNKNOWN);

            return new LoanResult<>(null, errorResult);
        }
    }

}
