package my.homework.country;

import com.google.common.cache.Cache;
import my.homework.settings.GeoIpClientSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Strings.isNullOrEmpty;

class CountryCodeResolverImpl implements CountryCodeResolver {

    private static final Logger logger = LoggerFactory.getLogger(CountryCodeResolverImpl.class);

    private final GeoIpClientSettings geoIpClientSettings;
    private final Cache<String, CountryInfo> countriesCache;

    CountryCodeResolverImpl(
        GeoIpClientSettings geoIpClientSettings,
        Cache<String, CountryInfo> countriesCache
    ) {
        this.geoIpClientSettings = geoIpClientSettings;
        this.countriesCache = countriesCache;
    }

    @Override
    public String resolve(String ipAddress) {
        try {
            logger.debug("starting resolving country by ip address; ipAddress: {}", ipAddress);
            CountryInfo countryInfo = countriesCache.getIfPresent(ipAddress);
            logger.debug("country resolving has succeeded; countryInfo: {}", countryInfo);

            String countryCode = countryInfo.getCountryCode();
            return isNullOrEmpty(countryCode) ? geoIpClientSettings.getDefaultCountryCode() : countryCode;
        } catch (Exception exception) {
            String errorFormatted = String.format(
                "an error occurred during country resolving due to error: %s, returning default value: %s",
                exception.getMessage(),
                geoIpClientSettings.getDefaultCountryCode()
            );
            logger.error(errorFormatted);
            return geoIpClientSettings.getDefaultCountryCode();
        }
    }
}
