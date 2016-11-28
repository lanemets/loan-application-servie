package my.homework.security;

import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
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
        http.authorizeRequests()
            .antMatchers("/apply", "/loans_approved/**")
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .authorizeRequests()
            .antMatchers("/h2-console/**")
            .permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterAfter(throttlingRequestFilter, BasicAuthenticationFilter.class);
    }
}
