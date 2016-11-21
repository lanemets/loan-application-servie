package my.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LoanServiceConfiguration {

    @Bean
    public LoanService loanService(LoanDao loanDao) {
        return new LoanServiceImpl(loanDao);
    }

    @Bean
    public LoanDao loanDao(DataSource dataSource) {
        return new LoanDaoImpl(dataSource);
    }

}
