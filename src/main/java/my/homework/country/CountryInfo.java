package my.homework.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryInfo {

    private String as;
    private String city;
    private String country;
    private String countryCode;
    private String isp;
    private String lat;
    private String lon;
    private String org;
    private String query;
    private String region;
    private String regionName;
    private String status;
    private String timezone;
    private String zip;

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        CountryInfo that = (CountryInfo) o;
        return Objects.equal(as, that.as) &&
            Objects.equal(city, that.city) &&
            Objects.equal(country, that.country) &&
            Objects.equal(countryCode, that.countryCode) &&
            Objects.equal(isp, that.isp) &&
            Objects.equal(lat, that.lat) &&
            Objects.equal(lon, that.lon) &&
            Objects.equal(org, that.org) &&
            Objects.equal(query, that.query) &&
            Objects.equal(region, that.region) &&
            Objects.equal(regionName, that.regionName) &&
            Objects.equal(status, that.status) &&
            Objects.equal(timezone, that.timezone) &&
            Objects.equal(zip, that.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
            as,
            city,
            country,
            countryCode,
            isp,
            lat,
            lon,
            org,
            query,
            region,
            regionName,
            status,
            timezone,
            zip
        );
    }

    @Override
    public String toString() {
        return "CountryInfo{" +
            "as='" + as + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", countryCode='" + countryCode + '\'' +
            ", isp='" + isp + '\'' +
            ", lat='" + lat + '\'' +
            ", lon='" + lon + '\'' +
            ", org='" + org + '\'' +
            ", query='" + query + '\'' +
            ", region='" + region + '\'' +
            ", regionName='" + regionName + '\'' +
            ", status='" + status + '\'' +
            ", timezone='" + timezone + '\'' +
            ", zip='" + zip + '\'' +
            '}';
    }
}
