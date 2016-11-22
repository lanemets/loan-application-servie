package my.homework.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class EventDaoConfiguration {

    @Bean
    public EventDao eventDao(DataSource dataSource) {
        return new EventDaoImpl(dataSource);
    }

}
