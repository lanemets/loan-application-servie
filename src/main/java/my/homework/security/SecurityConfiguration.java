package my.homework.security;

import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@Import(SecurityConfiguration.HttpSecurityConfiguration.class)
public class SecurityConfiguration {

    private ThrottlingRequestFilter throttlingRequestFilter;

    public SecurityConfiguration(
        CountryCodeResolver countryCodeResolver,
        ThrottlingRequestSettings throttlingRequestSettings
    ) {
        this.throttlingRequestFilter = throttlingRequestFilter(countryCodeResolver, throttlingRequestSettings);
    }

    private static ThrottlingRequestFilter throttlingRequestFilter(
        CountryCodeResolver countryCodeResolver,
        ThrottlingRequestSettings throttlingRequestSettings
    ) {
        return new ThrottlingRequestFilter(countryCodeResolver, throttlingRequestSettings);
    }

    @Bean
    public FilterRegistrationBean throttlingFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(throttlingRequestFilter);
        filterRegistrationBean.addUrlPatterns("/apply");

        return filterRegistrationBean;
    }

    @Configuration
    @EnableWebSecurity
    static class HttpSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

            http.csrf().disable();
            http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        }
    }
}
