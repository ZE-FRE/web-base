package cn.zefre.base.web.advice;

import cn.zefre.base.web.response.UniformResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 系统内部异常处理
 *
 * @author pujian
 * @date 2022/9/19 21:26
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerExceptionAdvice {

    /**
     * 全局异常
     */
    @ExceptionHandler(Exception.class)
    public UniformResponse exception(Exception e) {
        log.error(e.getMessage());
        return UniformResponse.error();
    }

}
