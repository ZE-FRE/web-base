package cn.zefre.base.web.annotation;

import cn.zefre.base.web.response.UniformResponse;

import java.lang.annotation.*;

/**
 * 包装controller接口返回值，返回统一数据类型
 * 既可以标注类，也可以标注方法
 *
 * @author pujian
 * @date 2022/1/25 14:12
 * @see UniformResponse
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapResponse {
}
