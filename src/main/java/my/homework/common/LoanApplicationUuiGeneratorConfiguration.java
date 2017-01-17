package my.homework.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoanApplicationUuiGeneratorConfiguration {

    @Bean
    public MdcWrapper mdcWrapper() {
        return new MdcWrapper();
    }

    @Bean
    public LoanApplicationUuidGenerator loanApplicationUuidGenerator() {
        return new LoanApplicationUuidGenerator();
    }
}
