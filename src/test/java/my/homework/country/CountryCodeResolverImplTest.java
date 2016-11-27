package my.homework.country;

import my.homework.GeoIpClientSettings;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.SocketTimeoutException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class CountryCodeResolverImplTest {

    @InjectMocks
    private CountryCodeResolverImpl countryCodeResolver;
    @Mock
    private GeoIpClientSettings geoIpClientSettings;
    @Mock
    private RestTemplate restTemplate;

    @BeforeTest
    public void setUp() {
        initMocks(this);
        when(geoIpClientSettings.getUrl()).thenReturn(TEST_URL);
        when(geoIpClientSettings.getDefaultCountryCode()).thenReturn(TEST_DEFAULT_COUNTRY_CODE);
    }

    @Test(dataProvider = "resolveDataProvider")
    public void testResolve(String ipAddress, CountryInfo countryInfo) throws Exception {
        when(restTemplate.getForObject(eq(TEST_URL), eq(CountryInfo.class), eq(ipAddress))).thenReturn(countryInfo);

        countryCodeResolver.resolve(ipAddress);

        verify(restTemplate, times(1)).getForObject(
            eq(TEST_URL),
            eq(CountryInfo.class),
            eq(ipAddress)
        );
    }

    @DataProvider
    public static Object[][] resolveDataProvider() {
        return new Object[][]{
            {
                "287.10.16.10",
                new CountryInfo(TEST_COUNTRY_CODE)
            }
        };
    }

    @Test
    public void testResolveByDefault() {
        String ipAddress = "287.10.16.10";
        doThrow(SocketTimeoutException.class)
            .when(restTemplate)
            .getForObject(
                eq(TEST_URL),
                eq(CountryInfo.class),
                eq(ipAddress)
            );

        assertEquals(countryCodeResolver.resolve(ipAddress), TEST_DEFAULT_COUNTRY_CODE);
    }

    private static final String TEST_URL = "http://api.geo.com";
    private static final String TEST_COUNTRY_CODE = "en";
    private static final String TEST_DEFAULT_COUNTRY_CODE = "lv";

}