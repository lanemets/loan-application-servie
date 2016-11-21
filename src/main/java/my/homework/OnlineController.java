package my.homework;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineController {

    @RequestMapping(
        value = "/apply",
        method = RequestMethod.PUT
    )
    public LoanResult<?> apply(@RequestBody LoanApplyingRequest loanApplyingRequest) {
        return null;
    }

}
