package cn.zefre.base.validation.constraint;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * IPV4地址约束
 *
 * @author pujian
 * @date 2021/11/1 15:15
 */
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IPV4Validator.class)
@Documented
public @interface IPV4 {
    String message() default "错误的IPV4地址";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * ipv4正则
     */
    String regex() default "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
}
