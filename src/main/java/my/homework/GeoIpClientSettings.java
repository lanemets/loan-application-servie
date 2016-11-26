package my.homework;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "api.geo.ip")
public class GeoIpClientSettings {

    private String url;
    private String defaultContryCode;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDefaultContryCode() {
        return defaultContryCode;
    }

    public void setDefaultContryCode(String defaultContryCode) {
        this.defaultContryCode = defaultContryCode;
    }
}
