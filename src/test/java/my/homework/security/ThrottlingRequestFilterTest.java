package my.homework.security;

import com.github.bucket4j.Bucket;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ThrottlingRequestFilterTest {

    private ThrottlingRequestFilter throttlingRequestFilter;
    @Mock
    private CountryCodeResolver countryCodeResolver;
    @Mock
    private ThrottlingRequestSettings throttlingRequestSettings;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        initThrottlingRequestSettings();

        throttlingRequestFilter = new ThrottlingRequestFilter(countryCodeResolver, throttlingRequestSettings);
    }

    @Test(dataProvider = "filterDataProvider")
    public void testFilter(
        String path,
        String ipAddress,
        String countryCode,
        Bucket mockBucket,
        boolean bucketConsumes
    ) throws IOException, ServletException {
        HttpServletRequest mockHttpServletRequest = mockHttpRequest(path, ipAddress);
        HttpServletResponse mockHttpServletResponse = mockHttpServletResponse();

        HttpSession mockHttpSession = mockSession(mockHttpServletRequest);
        when(mockHttpSession.getAttribute(eq("request_" + countryCode))).thenReturn(mockBucket);

        when(countryCodeResolver.resolve(eq(ipAddress))).thenReturn(countryCode);

        FilterChain mockFilterChain = mock(FilterChain.class);

        throttlingRequestFilter.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        verify(countryCodeResolver).resolve(eq(ipAddress));
        verify(mockHttpSession).getAttribute(eq("request_" + countryCode));

        verify(mockBucket).tryConsumeSingleToken();
        if (bucketConsumes) {
            verify(mockHttpServletRequest).setAttribute(eq("country_code"), eq(countryCode));
            verify(mockFilterChain).doFilter(
                eq((ServletRequest) mockHttpServletRequest),
                eq((ServletResponse) mockHttpServletResponse)
            );
        } else {
            verify(mockHttpServletResponse).setStatus(eq(429));
        }
    }

    @DataProvider
    public static Object[][] filterDataProvider() {
        return new Object[][]{
            {
                "/apply",
                "268.90.45.1",
                "US",
                createBucket(true),
                true
            },
            {
                "/apply",
                "268.90.45.1",
                "US",
                createBucket(false),
                false
            }
        };
    }


    private void initThrottlingRequestSettings() {
        when(throttlingRequestSettings.getMaxCapacity()).thenReturn(10);
        when(throttlingRequestSettings.getPeriod()).thenReturn(1);
        when(throttlingRequestSettings.getTimeUnit()).thenReturn("MINUTES");
    }

    private HttpServletRequest mockHttpRequest(String path, String ipAddress) {
        HttpServletRequest mockServletRequest = mock(HttpServletRequest.class);
        when(mockServletRequest.getPathInfo()).thenReturn(path);
        when(mockServletRequest.getRemoteAddr()).thenReturn(ipAddress);
        return mockServletRequest;
    }

    private HttpServletResponse mockHttpServletResponse() throws IOException {
        HttpServletResponse mockHttpServletResponse = mock(HttpServletResponse.class);
        when(mockHttpServletResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        return mockHttpServletResponse;
    }

    private HttpSession mockSession(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = mock(HttpSession.class);
        when(httpServletRequest.getSession(anyBoolean())).thenReturn(httpSession);

        return httpSession;
    }

    private static Bucket createBucket(boolean tryConsume) {
        Bucket mockBucket = mock(Bucket.class);
        when(mockBucket.tryConsumeSingleToken()).thenReturn(tryConsume);

        return mockBucket;
    }
}