package my.homework.service;

import my.homework.dao.BlackListDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlackListServiceConfiguration {

    @Bean
    public BlackListService blackListService(BlackListDao blackListDao) {
        return new BlackListServiceImpl(blackListDao);
    }

}
