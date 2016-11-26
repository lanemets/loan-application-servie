package my.homework;

import com.github.bucket4j.Bucket;
import com.github.bucket4j.Buckets;
import my.homework.country.CountryCodeResolver;
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

class ThrottlingRequestFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ThrottlingRequestFilter.class);

    private final CountryCodeResolver countryCodeResolver;

    public ThrottlingRequestFilter(CountryCodeResolver countryCodeResolver) {
        this.countryCodeResolver = countryCodeResolver;
    }

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        String localAddr = servletRequest.getLocalAddr();
        String resolve = countryCodeResolver.resolve(localAddr);

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpRequest.getSession(true);
        //add country code to throttler value
        Bucket bucket = (Bucket) session.getAttribute("throttler");
        if (bucket == null) {
            bucket = Buckets.withNanoTimePrecision()
                .withLimitedBandwidth(1, TimeUnit.MINUTES, 1)
                .build();
            session.setAttribute("throttler", bucket);
        }

        if (bucket.tryConsumeSingleToken()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setContentType("text/plain");
            httpResponse.setStatus(429);
            httpResponse.getWriter().append("Too many requests");
        }
    }
}
