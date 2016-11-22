package my.homework.service;

import my.homework.dao.BlackListDaoConfiguration;
import my.homework.dao.BlackListedDao;
import my.homework.service.BlackListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BlackListDaoConfiguration.class)
public class BlackListServiceConfiguration {

    @Bean
    public BlackListService blackListService(BlackListedDao blackListedDao) {
        return new BlackListServiceImpl(blackListedDao);
    }

}
