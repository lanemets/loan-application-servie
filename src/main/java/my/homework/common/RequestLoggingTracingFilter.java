package my.homework.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


import static my.homework.common.TracingContextUtils.createTracingContext;

public class RequestLoggingTracingFilter extends CommonsRequestLoggingFilter {

    private final MdcWrapper mdcWrapper;
    private final UuidGenerator uuidGenerator;

    public RequestLoggingTracingFilter(
        MdcWrapper mdcWrapper,
        UuidGenerator uuidGenerator
    ) {
        this.mdcWrapper = mdcWrapper;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return super.shouldLog(request);
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        mdcWrapper.enter(createTracingContext(request, uuidGenerator));
        super.beforeRequest(request, message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        super.afterRequest(request, message);
        mdcWrapper.exit();
    }
}
