package my.homework.security;

import my.homework.common.MdcWrapper;
import my.homework.common.RequestLoggingTracingFilter;
import my.homework.common.UuidGenerator;
import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ThrottlingRequestFilter throttlingRequestFilter;
    private final RequestLoggingTracingFilter requestLoggingTracingFilter;

    public SecurityConfiguration(
        CountryCodeResolver countryCodeResolver,
        ThrottlingRequestSettings throttlingRequestSettings,
        MdcWrapper mdcWrapper,
        UuidGenerator uuidGenerator
    ) {
        this.throttlingRequestFilter = new ThrottlingRequestFilter(countryCodeResolver, throttlingRequestSettings);
        this.requestLoggingTracingFilter = new RequestLoggingTracingFilter(mdcWrapper, uuidGenerator);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().hasRole("USER").and().httpBasic();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(throttlingRequestFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(requestLoggingTracingFilter, ThrottlingRequestFilter.class);

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("Qwerty12")
            .roles("USER");
    }
}
