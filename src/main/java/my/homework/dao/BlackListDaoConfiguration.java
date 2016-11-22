package my.homework.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BlackListDaoConfiguration {

    @Bean
    public BlackListedDao blackListedDao(DataSource dataSource) {
        return new BlackListedDaoImpl(dataSource);
    }

}
