package my.homework.country;

import my.homework.settings.GeoIpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CountryCodeResolverConfiguration {

    @Bean
    public CountryCodeResolver countryCodeResolver(
        GeoIpClientSettings geoIpClientSettings,
        RestTemplate restTemplate
    ) {
        return new CountryCodeResolverImpl(geoIpClientSettings, restTemplate);
    }

}
