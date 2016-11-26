package my.homework;

import com.google.common.util.concurrent.RateLimiter;
import my.homework.country.CountryCodeResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private ThrottlingRequestFilter throttlingRequestFilter;
    private CountryCodeResolver countryCodeResolver;

    public SecurityConfiguration(CountryCodeResolver countryCodeResolver) {
        this.throttlingRequestFilter = throttlingRequestFilter(countryCodeResolver);
    }

    private ThrottlingRequestFilter throttlingRequestFilter(CountryCodeResolver countryCodeResolver) {
        return new ThrottlingRequestFilter(countryCodeResolver);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable();

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);

        http.addFilterBefore(throttlingRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
