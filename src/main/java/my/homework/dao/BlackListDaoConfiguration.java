package my.homework.dao;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlackListDaoConfiguration {

    @Bean
    public BlackListDao blackListedDao(DataSource dataSource) {
        return new BlackListDaoImpl(dataSource);
    }

}
