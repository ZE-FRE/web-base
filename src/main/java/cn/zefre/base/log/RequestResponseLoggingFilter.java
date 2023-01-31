package cn.zefre.base.log;

import cn.zefre.base.config.BaseProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

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
public class RequestResponseLoggingFilter extends AbstractRequestLoggingFilter {

    private static final String BEFORE_MESSAGE_PREFIX = "Before request ";

    private static final String BEFORE_MESSAGE_SUFFIX = "";

    private static final String AFTER_MESSAGE_PREFIX = "After  request ";

    private static final String AFTER_MESSAGE_SUFFIX = "";

    private static final String JSON_TYPE = "application/json";

    private BaseProperties.MvcLog mvcLog;

    /**
     * 是否打印请求日志
     */
    public boolean isLogRequest() {
        return mvcLog.isLogRequest();
    }

    /**
     * 是否打印响应日志
     */
    public boolean isLogResponse() {
        return mvcLog.isLogResponse();
    }

    public RequestResponseLoggingFilter() {
        this(new BaseProperties.MvcLog());
    }

    public RequestResponseLoggingFilter(BaseProperties.MvcLog mvcLog) {
        this.mvcLog = mvcLog;
        // 设置默认参数
        this.setIncludeQueryString(true);
        this.setIncludePayload(true);
        this.setMaxPayloadLength(2000);
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return mvcLog.isEnable();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!shouldLog(request)) {
            // 不需要打印日志
            filterChain.doFilter(request, response);
            return;
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;
        if (isLogRequest() && isIncludePayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }
        HttpServletResponse responseToUse = isLogResponse() ? new ContentCachingResponseWrapper(response) : response;
        if (isFirstRequest) {
            beforeRequest(requestToUse, createMessage(requestToUse, BEFORE_MESSAGE_PREFIX, BEFORE_MESSAGE_SUFFIX));
        }
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (!isAsyncStarted(requestToUse)) {
                String requestAfterMessage = createRequestAfterMessage(requestToUse);
                String responseMessage = createResponseMessage(responseToUse);
                stopWatch.stop();
                // 调用花费时间
                long costTime = stopWatch.getLastTaskInfo().getTimeMillis();
                log.info(String.format("%s花费时间：%s(毫秒)%s", requestAfterMessage, costTime, responseMessage));
            }
        }
    }

    private String createRequestAfterMessage(HttpServletRequest request) {
        String blank = "  ";
        if (isLogRequest()) {
            return createMessage(request, AFTER_MESSAGE_PREFIX, AFTER_MESSAGE_SUFFIX) + blank;
        } else {
            return blank;
        }
    }

    private String createResponseMessage(HttpServletResponse response) throws IOException {
        String responseMessage = "";
        if (!isLogResponse()) {
            return responseMessage;
        }
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Assert.notNull(responseWrapper, "ContentCachingResponseWrapper not found");
        // 只记录响应体为json的
        if (responseWrapper.getContentType() != null &&
                JSON_TYPE.contains(responseWrapper.getContentType())) {
            // 获取响应信息
            byte[] responseBuf = responseWrapper.getContentAsByteArray();
            responseMessage = "response=" + new String(responseBuf);
        }
        // 需要将response body复制到响应流，才能正常响应response body信息
        responseWrapper.copyBodyToResponse();
        return responseMessage;
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
