package my.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineController {

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
        //blacklisted
        //throttled
        //???
        loanService.apply(loanApplyingRequest);
        return new LoanResult<String>("OK", null);
    }

}
