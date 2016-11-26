package my.homework.country;

import my.homework.GeoIpClientSettings;
import my.homework.RestClientConfiguration;
import org.springframework.web.client.RestTemplate;

class CountryCodeResolverImpl implements CountryCodeResolver {

    private final RestClientConfiguration restClientConfiguration;
    private final GeoIpClientSettings geoIpClientSettings;
    private final RestTemplate restTemplate;

    public CountryCodeResolverImpl(
        RestClientConfiguration restClientConfiguration,
        GeoIpClientSettings geoIpClientSettings,
        RestTemplate restTemplate
    ) {
        this.restClientConfiguration = restClientConfiguration;
        this.geoIpClientSettings = geoIpClientSettings;
        this.restTemplate = restTemplate;
    }

    @Override
    public String resolve(String ipAddress) {
        try {
            CountryInfo countryInfo = restTemplate.getForObject(
                geoIpClientSettings.getUrl(),
                CountryInfo.class,
                ipAddress
            );
        } catch (Exception exception) {

        }
    }
}
