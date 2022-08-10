package learn.utils;

import jakarta.enterprise.context.Dependent;

@Dependent
public class RequestCountUtil {
    private int requestCount;

    public RequestCountUtil() {
        this.requestCount = 0;
    }

    public int next() {
        return ++this.requestCount;
    }
}
