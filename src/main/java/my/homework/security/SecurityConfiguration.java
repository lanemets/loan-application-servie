package my.homework.security;

import my.homework.country.CountryCodeResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http.csrf().disable();

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //only for application request
        http.addFilterAfter(throttlingRequestFilter, BasicAuthenticationFilter.class)
            .antMatcher("/apply");
    }
}
