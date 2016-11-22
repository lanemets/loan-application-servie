package my.homework.service;

import my.homework.dao.LoanDao;
import my.homework.dao.LoanDaoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LoanDaoConfiguration.class)
public class LoanServiceConfiguration {

    @Bean
    public LoanService loanService(LoanDao loanDao) {
        return new LoanServiceImpl(loanDao);
    }
}
