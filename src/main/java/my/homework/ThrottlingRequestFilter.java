package my.homework;

import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

class ThrottlingRequestFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ThrottlingRequestFilter.class);

    @Override
    public void doFilter(
        ServletRequest servletRequest,
        ServletResponse servletResponse,
        FilterChain filterChain
    ) throws IOException, ServletException {
        String localAddr = servletRequest.getLocalAddr();
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
