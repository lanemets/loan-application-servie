package my.homework.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "api.geo.ip")
public class GeoIpClientSettings {

    private String url;
    private String defaultCountryCode;
    private int cacheMaximumSize;

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

    public int getCacheMaximumSize() {
        return cacheMaximumSize;
    }

    public void setCacheMaximumSize(int cacheMaximumSize) {
        this.cacheMaximumSize = cacheMaximumSize;
    }
}
