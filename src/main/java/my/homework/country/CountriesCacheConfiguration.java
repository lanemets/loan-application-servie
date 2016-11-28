package my.homework.country;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import my.homework.settings.GeoIpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountriesCacheConfiguration {

    @Bean
    public Cache<String, CountryInfo> countryInfoCache(GeoIpClientSettings geoIpClientSettings) {
        return CacheBuilder.<String, CountryInfo>newBuilder()
            .maximumSize(geoIpClientSettings.getCacheMaximumSize())
            .build();
    }
}
