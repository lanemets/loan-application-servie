package my.homework.service;

import my.homework.dao.LoanApplicationDao;
import my.homework.dao.LoanDaoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    LoanDaoConfiguration.class,
    BlackListServiceConfiguration.class
})
public class LoanServiceConfiguration {

    @Bean
    public LoanService loanService(
        LoanApplicationDao loanApplicationDao,
        BlackListService blackListService
    ) {
        return new LoanServiceImpl(loanApplicationDao, blackListService);
    }
}
