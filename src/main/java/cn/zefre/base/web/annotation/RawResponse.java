package cn.zefre.base.web.annotation;

import java.lang.annotation.*;

/**
 * 不包装controller接口返回值
 *
 * @author pujian
 * @date 2022/1/25 14:13
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RawResponse {
}
