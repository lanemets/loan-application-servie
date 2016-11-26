package my.homework.country;

import my.homework.GeoIpClientSettings;
import my.homework.RestClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CountryCodeResolverConfiguration {

    @Bean
    public CountryCodeResolver countryCodeResolver(
        RestClientConfiguration restClientConfiguration,
        GeoIpClientSettings geoIpClientSettings,
        RestTemplate restTemplate
    ) {
        return new CountryCodeResolverImpl(restClientConfiguration, geoIpClientSettings, restTemplate);
    }

}
