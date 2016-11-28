package my.homework.country;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import my.homework.settings.GeoIpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CountryCodeResolverConfiguration {

    @Bean
    public CountryCodeResolver countryCodeResolver(
        GeoIpClientSettings geoIpClientSettings,
        Cache<String, CountryInfo> countriesCache
    ) {
        return new CountryCodeResolverImpl(
            geoIpClientSettings,
            countriesCache
        );
    }

}
