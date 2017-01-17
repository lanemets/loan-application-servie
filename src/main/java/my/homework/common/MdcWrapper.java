package my.homework.common;

import org.slf4j.MDC;

public class MdcWrapper {

    public void enter(LoanTracingContext loanTracingContext) {
        MDC.put("traceId", loanTracingContext.getTraceId());
        MDC.put("spanId", loanTracingContext.getSpanId());
        MDC.put("parentSpanId", loanTracingContext.getParentSpanId());
    }

    public void exit() {
        MDC.remove("parentSpanId");
        MDC.remove("spanId");
        MDC.remove("traceId");
    }
}
