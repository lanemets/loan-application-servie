package my.homework.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LoanDaoConfiguration {

    @Bean
    public LoanDao loanDao(DataSource dataSource) {
        return new LoanDaoImpl(dataSource);
    }

}
