package cn.zefre.base.web.advice;

import cn.zefre.base.web.response.UniformResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 请求参数异常处理
 *
 * @author pujian
 * @date 2022/1/25 11:01
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestParamExceptionAdvice {

    /**
     * 处理controller层普通参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public UniformResponse validationException(BindException e) {
        List<ObjectError> allErrors = e.getAllErrors();
        String errorMsg = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        log.info("请求参数错误：{}", errorMsg);
        return UniformResponse.validationFailed(allErrors.get(0).getDefaultMessage());
    }

    /**
     * 处理controller层@ResponseBody参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public UniformResponse validationException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String errorMsg = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        log.info("请求参数错误：{}", errorMsg);
        return UniformResponse.validationFailed(allErrors.get(0).getDefaultMessage());
    }

    /**
     * 除controller层以外的方法参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public UniformResponse methodValidationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMsg = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        log.info("方法参数校验异常：{}", errorMsg);
        return UniformResponse.validationFailed(violations.iterator().next().getMessage());
    }

    /**
     * http请求常见错误处理
     */
    @ExceptionHandler(ServletException.class)
    public UniformResponse httpRequestException(ServletException e) {
        log.warn(e.getMessage());
        return UniformResponse.error(e.getMessage());
    }

}



