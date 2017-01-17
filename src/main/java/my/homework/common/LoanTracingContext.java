package my.homework.common;

import com.google.common.base.Objects;

public class LoanTracingContext {

    private final String traceId;
    private final String spanId;
    private final String parentSpanId;

    public LoanTracingContext(String traceId, String spanId, String parentSpanId) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoanTracingContext that = (LoanTracingContext) o;
        return Objects.equal(traceId, that.traceId) &&
            Objects.equal(spanId, that.spanId) &&
            Objects.equal(parentSpanId, that.parentSpanId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(traceId, spanId, parentSpanId);
    }
}
