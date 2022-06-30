package cn.zefre.base.web.annotation;

import java.lang.annotation.*;

/**
 * 不包装controller接口返回值
 * 当{@link WrapResponse}标注在类上时，用此注解标注某方法，使其返回原本数据类型
 *
 * @author pujian
 * @date 2022/1/25 14:13
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RawResponse {
}
