package cn.zefre.base.web.advice;

import cn.zefre.base.web.annotation.RawResponse;
import cn.zefre.base.web.annotation.WrapResponse;
import cn.zefre.base.web.response.UniformResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * 包装controller接口返回数据
 *
 * @author pujian
 * @date 2022/1/25 14:24
 */
@ControllerAdvice
public class WrapResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> methodReturnType = returnType.getMethod().getReturnType();
        boolean returnTypeIsVoid = methodReturnType.isAssignableFrom(Void.class);
        // 类上或方法上是否有@WrapResponse注解标识
        boolean hasWrapResponse = returnType.getDeclaringClass().isAnnotationPresent(WrapResponse.class)
                || returnType.hasMethodAnnotation(WrapResponse.class);
        // 方法上是否有@RawResponse注解标识
        boolean hasRawResponse = returnType.hasMethodAnnotation(RawResponse.class);
        return hasWrapResponse && !hasRawResponse && !returnTypeIsVoid;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
            return objectMapper.writeValueAsString(UniformResponse.ok(body));
        } else if (body instanceof UniformResponse) {
            return body;
        } else {
            return UniformResponse.ok(body);
        }
    }


}
