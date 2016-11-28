package my.homework.controller;

import my.homework.LoanApplicationRequest;
import my.homework.common.UuidGenerator;
import my.homework.constant.ErrorResult;
import my.homework.constant.ErrorTypes;
import my.homework.constant.LoanResult;
import my.homework.service.LoanApplication;
import my.homework.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
class OnlineController {

    private static final Logger logger = LoggerFactory.getLogger(OnlineController.class);

    private final LoanService loanService;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public OnlineController(
        LoanService loanService,
        UuidGenerator loanApplicationUuidGenerator
    ) {
        this.loanService = loanService;
        this.uuidGenerator = loanApplicationUuidGenerator;
    }

    @RequestMapping(
        value = "/apply",
        method = RequestMethod.PUT,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoanResult<?> apply(
        HttpServletRequest httpServletRequest,
        @RequestBody LoanApplicationRequest loanApplicationRequest
    ) {
        try {
            logger.debug("starting loan application request processing; personalId: {}", loanApplicationRequest.getPersonalId());
            String countryCode = String.valueOf(httpServletRequest.getAttribute("country_code"));

            String requestUuid = uuidGenerator.generate();
            loanService.apply(loanApplicationRequest, countryCode, requestUuid);
            logger.debug(
                "loan application request processed successfully; personalId: {}, requestUid: {}",
                loanApplicationRequest.getPersonalId(),
                requestUuid
            );
            return new LoanResult<>(requestUuid, null);
        } catch (Exception exception) {
            logger.error(
                String.format(
                    "unexpected error has occurred during application process; personalId: %f",
                    loanApplicationRequest.getPersonalId()
                ),
                exception
            );

            return new LoanResult<String>(null, createUnknownErrorResult(exception));
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
            List<LoanApplication> allLoansApproved = loanService.getAllLoansApproved(null);
            logger.debug("all loans approved retrieved successfully; loans retrieved count: {}", allLoansApproved.size());
            return new LoanResult<>(allLoansApproved, null);
        } catch (Exception exception) {
            logger.error("unexpected error has occurred during all approved loans retrieval;", exception);
            return new LoanResult<String>(null, createUnknownErrorResult(exception));
        }
    }

    @RequestMapping(
        value = "/loans_approved/{personal_id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoanResult<?> getAllLoansApprovedByPersonalId(@PathVariable("personal_id") String personalId) {
        try {
            logger.debug("starting all person's loans approved retrieving; personalId: {}", personalId);
            List<LoanApplication> allLoansApproved = loanService.getAllLoansApproved(Long.valueOf(personalId));
            logger.debug("person's loans approved has been retrieved successfully; loans retrieved count: {}", allLoansApproved.size());
            return new LoanResult<>(allLoansApproved, null);
        } catch (Exception exception) {
            logger.error("unexpected error has occurred during all approved loans retrieval;", exception);
            return new LoanResult<String>(null, createUnknownErrorResult(exception));
        }
    }

    @RequestMapping(
        value = "/loans_applications/{application_uid}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LoanResult<?> getLoanApplicationByUid(@PathVariable("application_uid") String applicationUid) {
        try {
            logger.debug("starting retrieving loan application by uid; uid: {}", applicationUid);
            LoanApplication loanApplicationByUid = loanService.getLoanApplicationByUid(applicationUid);
            logger.debug("loan application has been retrieved successfully; loanApplication: {}", loanApplicationByUid);
            return new LoanResult<>(loanApplicationByUid, null);
        } catch (Exception exception) {
            logger.error("unexpected error has occurred during loan application retrieval;", exception);
            return new LoanResult<String>(null, createUnknownErrorResult(exception));
        }
    }

    private static ErrorResult createUnknownErrorResult(Exception exception) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setCode(ErrorTypes.UNKNOWN);
        errorResult.setMessage(exception.getMessage());

        return errorResult;
    }
}
