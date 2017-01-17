package my.homework.common;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.util.StringUtils.isEmpty;

class TracingContextUtils {

    static LoanTracingContext createTracingContext(HttpServletRequest httpServletRequest, UuidGenerator uuidGenerator) {
        String traceId = httpServletRequest.getHeader(TRACE_ID);
        String spanId = httpServletRequest.getHeader(SPAN_ID);
        String parentSpanId = httpServletRequest.getHeader(PARENT_SPAN_ID);

        return new LoanTracingContext(
            isEmpty(traceId) ? uuidGenerator.generate(TRACE_ID) : traceId,
            uuidGenerator.generate(SPAN_ID),
            isEmpty(parentSpanId) ? "" : spanId
        );
    }

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String SPAN_ID = "X-B3-SpanId";
    private static final String PARENT_SPAN_ID = "X-B3-ParentSpanId";
}
