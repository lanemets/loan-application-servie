package my.homework;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private ThrottlingRequestFilter throttlingRequestFilter;

    public SecurityConfiguration() {
        this.throttlingRequestFilter = throttlingRequestFilter();
    }

    private ThrottlingRequestFilter throttlingRequestFilter() {
        return new ThrottlingRequestFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable();

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(throttlingRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
