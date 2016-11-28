package my.homework.security;

import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

        http.addFilterAfter(throttlingRequestFilter, BasicAuthenticationFilter.class)
            .antMatcher("/apply");

        http.authorizeRequests().antMatchers("/**").authenticated().and().httpBasic();

        http.csrf().disable();
        http.headers().frameOptions().disable();

        http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    }
}
