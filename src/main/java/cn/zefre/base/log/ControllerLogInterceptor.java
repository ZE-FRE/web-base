package cn.zefre.base.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 打印controller方法的日志
 *
 * @author pujian
 * @date 2023/1/17 17:39
 */
@Slf4j
//@Component
public class ControllerLogInterceptor implements MethodInterceptor {

    @Resource
    private ObjectMapper objectMapper;

    //@Resource
    //private LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer;
    private LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private static final Set<Class<?>> exclusionClasses = new HashSet<>(Arrays.asList(HttpServletRequest.class, HttpServletResponse.class, MultipartFile.class));

    private boolean isExclusionClass(Object obj) {
        for (Class<?> exclusionClass : exclusionClasses) {
            if (exclusionClass.isAssignableFrom(obj.getClass())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "");
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Map<RequestParameterType, Object> requestInfo = extractRequestInfo(methodInvocation);
        Object requestParam = requestInfo.get(RequestParameterType.PARAMETER);
        Object requestBody = requestInfo.get(RequestParameterType.BODY);
        String requestParamStr = requestParam == null ? "" : "request parameter：" + objectMapper.writeValueAsString(requestParam);
        String requestBodyStr = requestBody == null ? "" : "request body：" + objectMapper.writeValueAsString(requestBody);

        Thread.currentThread().setName(Long.toString(System.currentTimeMillis()));
        String uri = request.getRequestURI();
        log.info("接口地址：{}，线程{}开始处理 {} {}", uri, Thread.currentThread().getName(), requestParamStr, requestBodyStr);

        Object response = methodInvocation.proceed();

        stopWatch.stop();
        // 调用花费时间
        long costTime = stopWatch.getLastTaskInfo().getTimeMillis();
        String res = response == null ? "" : objectMapper.writeValueAsString(response);
        log.info("接口地址：{}，线程{}处理完成 response body：{}，花费时间：{}(毫秒)", uri, Thread.currentThread().getName(), res, costTime);
        return response;
    }


    /**
     * 请求参数类型
     */
    enum RequestParameterType {
        /**
         * 请求参数
         */
        PARAMETER,
        /**
         * 请求体
         */
        BODY
    }

    /**
     * 提取请求传参信息
     *
     * @param methodInvocation methodInvocation
     * @return requestInfo
     * @author pujian
     * @date 2023/1/13 17:10
     */
    Map<RequestParameterType, Object> extractRequestInfo(MethodInvocation methodInvocation) {
        Map<RequestParameterType, Object> requestInfo = new HashMap<>();
        // 获取到方法
        Method method = methodInvocation.getMethod();
        // 方法接收到的实参
        Object[] args = methodInvocation.getArguments();
        // 取得方法的参数名
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        // 取得方法参数的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        // 请求参数
        Map<String, Object> requestParam = new HashMap<>();
        // 请求体
        Object requestBody = null;
        if (parameterNames != null && parameterNames.length > 0) {
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                Object parameterValue = args[i];
                if (parameterValue == null || isExclusionClass(parameterValue)) {
                    continue;
                }
                // 参数类型，是请求参数还是请求体，默认是请求参数
                RequestParameterType parameterType = RequestParameterType.PARAMETER;
                // 取得此参数上的注解
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                for (Annotation annotation : parameterAnnotation) {
                    Class<? extends Annotation> annotationClass = annotation.getClass();
                    if (PathVariable.class.isAssignableFrom(annotationClass)) {
                        String value = ((PathVariable) annotation).value();
                        parameterName = StringUtils.isNotEmpty(value) ? value : parameterName;
                    } else if (RequestParam.class.isAssignableFrom(annotationClass)) {
                        String value = ((RequestParam) annotation).value();
                        parameterName = StringUtils.isNotEmpty(value) ? value : parameterName;
                    } else if (ModelAttribute.class.isAssignableFrom(annotationClass)) {
                        String value = ((ModelAttribute) annotation).value();
                        parameterName = StringUtils.isNotEmpty(value) ? value : parameterName;
                    } else if (RequestBody.class.isAssignableFrom(annotationClass)) {
                        requestBody = parameterValue;
                        parameterType = RequestParameterType.BODY;
                    }
                }
                if (parameterType == RequestParameterType.PARAMETER) {
                    requestParam.put(parameterName, parameterValue);
                }
            }
        }
        if (!requestParam.isEmpty()) {
            requestInfo.put(RequestParameterType.PARAMETER, requestParam);
        }
        if (requestBody != null) {
            requestInfo.put(RequestParameterType.BODY, requestBody);
        }
        return requestInfo;
    }

}
