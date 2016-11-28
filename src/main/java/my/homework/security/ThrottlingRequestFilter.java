package my.homework.security;

import com.github.bucket4j.Bucket;
import com.github.bucket4j.Buckets;
import my.homework.country.CountryCodeResolver;
import my.homework.settings.ThrottlingRequestSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Strings.isNullOrEmpty;

class ThrottlingRequestFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ThrottlingRequestFilter.class);

    private final CountryCodeResolver countryCodeResolver;
    private final ThrottlingRequestSettings throttlingRequestSettings;

    ThrottlingRequestFilter(
        CountryCodeResolver countryCodeResolver,
        ThrottlingRequestSettings throttlingRequestSettings
    ) {
        this.countryCodeResolver = countryCodeResolver;
        this.throttlingRequestSettings = throttlingRequestSettings;
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (httpRequest.getServletPath().equals("/apply") || httpRequest.getPathInfo().equals("/apply")) {

            logger.debug("starting throttling check");

            String ipAddress = httpRequest.getHeader(PROXY_HEADER);
            if (isNullOrEmpty(ipAddress)) {
                ipAddress = httpRequest.getRemoteAddr();
            }

            logger.debug("received request; ipAddress: {}", ipAddress);

            String countryCode = countryCodeResolver.resolve(ipAddress);

            logger.debug("resolved country; countryCode: {}", countryCode);

            HttpSession session = httpRequest.getSession(true);
            Bucket bucket = (Bucket) session.getAttribute(requestThrottlingAttribute(countryCode));
            if (bucket == null) {
                bucket = Buckets.withNanoTimePrecision()
                    .withLimitedBandwidth(
                        throttlingRequestSettings.getMaxCapacity(),
                        TimeUnit.valueOf(throttlingRequestSettings.getTimeUnit()),
                        throttlingRequestSettings.getPeriod()
                    )
                    .build();
                session.setAttribute(requestThrottlingAttribute(countryCode), bucket);
            }

            if (bucket.tryConsumeSingleToken()) {
                servletRequest.setAttribute("country_code", countryCode);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setContentType("text/plain");
                httpResponse.setStatus(429);
                httpResponse.getWriter().append("Too many requests from one country");
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private static String requestThrottlingAttribute(String countryCode) {
        return String.format("request_%s", countryCode);
    }

    private static final String PROXY_HEADER = "X-FORWARDED-FOR";
}
