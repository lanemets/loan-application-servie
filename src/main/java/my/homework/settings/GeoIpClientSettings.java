package my.homework.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "api.geo.ip")
public class GeoIpClientSettings {

    private String url;
    private String defaultCountryCode;
    private int cacheExpirationDuration;
    private String cacheExpirationUnit;

    public int getCacheExpirationDuration() {
        return cacheExpirationDuration;
    }

    public void setCacheExpirationDuration(int cacheExpirationDuration) {
        this.cacheExpirationDuration = cacheExpirationDuration;
    }

    public String getCacheExpirationUnit() {
        return cacheExpirationUnit;
    }

    public void setCacheExpirationUnit(String cacheExpirationUnit) {
        this.cacheExpirationUnit = cacheExpirationUnit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDefaultCountryCode() {
        return defaultCountryCode;
    }

    public void setDefaultCountryCode(String defaultCountryCode) {
        this.defaultCountryCode = defaultCountryCode;
    }
}
