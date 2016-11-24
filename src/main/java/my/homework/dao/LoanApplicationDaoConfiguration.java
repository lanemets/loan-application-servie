package my.homework.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LoanApplicationDaoConfiguration {

    @Bean
    public LoanApplicationDao loanDao(DataSource dataSource) {
        return new LoanApplicationDaoImpl(dataSource);
    }

}
