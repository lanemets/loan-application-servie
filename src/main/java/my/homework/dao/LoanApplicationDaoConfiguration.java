package my.homework.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanApplicationDaoConfiguration {

    @Bean
    public LoanApplicationDao loanDao(DataSource dataSource) {
        return new LoanApplicationDaoImpl(dataSource);
    }

}
