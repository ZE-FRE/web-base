package cn.zefre.base.web.advice;

import cn.zefre.base.web.response.UniformResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author pujian
 * @date 2022/8/2 13:10
 */
@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 全局异常
     */
    @ExceptionHandler(Exception.class)
    public UniformResponse exception(Exception e) {
        log.error(e.getMessage());
        return UniformResponse.error();
    }

}
