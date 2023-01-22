package cn.zefre.base.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 打印请求与响应日志
 *
 * @author pujian
 * @date 2023/1/18 17:02
 */
@Slf4j
@Component
public class RequestResponseLoggingFilter extends AbstractRequestLoggingFilter {

    private static final String BEFORE_MESSAGE_PREFIX = "Before request ";

    private static final String BEFORE_MESSAGE_SUFFIX = "";

    private static final String AFTER_MESSAGE_PREFIX = "After  request ";

    private static final String AFTER_MESSAGE_SUFFIX = "";

    private static final String JSON_TYPE = "application/json";

    /**
     * 是否打印请求与响应日志
     */
    @Value("${spring.mvc.log.enabled:true}")
    private boolean enabled;

    public RequestResponseLoggingFilter() {
        // 设置默认参数
        this.setIncludeQueryString(true);
        this.setIncludePayload(true);
        this.setMaxPayloadLength(2000);
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return this.enabled;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        if (isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }
        if (!shouldLog(requestToUse)) {
            // 不需要打印日志
            filterChain.doFilter(requestToUse, response);
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ContentCachingResponseWrapper responseToUse = new ContentCachingResponseWrapper(response);
        if (isFirstRequest) {
            beforeRequest(requestToUse, createMessage(request, BEFORE_MESSAGE_PREFIX, BEFORE_MESSAGE_SUFFIX));
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        }
        finally {
            if (!isAsyncStarted(requestToUse)) {
                String requestMessage = createMessage(request, AFTER_MESSAGE_PREFIX, AFTER_MESSAGE_SUFFIX);
                String responseMessage = "";
                if (responseToUse.getContentType() != null && JSON_TYPE.contains(responseToUse.getContentType())) {
                    // 获取响应信息
                    byte[] responseBuf = responseToUse.getContentAsByteArray();
                    responseMessage = new String(responseBuf);
                }
                // 需要将response body复制到响应流，才能正常响应response body信息
                responseToUse.copyBodyToResponse();
                stopWatch.stop();
                // 调用花费时间
                long costTime = stopWatch.getLastTaskInfo().getTimeMillis();
                log.info("{}，花费时间：{}(毫秒)，response={}", requestMessage, costTime, responseMessage);
            }
        }
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        // just overriding,do nothing
    }

}
