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
public class CountriesCacheConfiguration {

    @Bean
    public Cache<String, CountryInfo> countryInfoCache(
        RestTemplate restTemplate,
        GeoIpClientSettings geoIpClientSettings
    ) {
        return CacheBuilder
            .<String, CountryInfo>newBuilder()
            .expireAfterWrite(
                geoIpClientSettings.getCacheExpirationDuration(),
                TimeUnit.valueOf(geoIpClientSettings.getCacheExpirationUnit())
            ).build(
            new CacheLoader<String, CountryInfo>() {
                @SuppressWarnings("NullableProblems")
                @Override
                public CountryInfo load(String ipAddress) throws Exception {
                    return restTemplate.getForObject(
                        geoIpClientSettings.getUrl(),
                        CountryInfo.class,
                        ipAddress
                    );
                }
            });
    }
}
