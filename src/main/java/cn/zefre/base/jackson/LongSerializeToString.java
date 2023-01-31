package cn.zefre.base.jackson;

import cn.zefre.base.jackson.serializer.LongToStringSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注明jackson序列化时把{@link Long}序列化为字符串
 *
 * @author pujian
 * @date 2023/1/29 18:46
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = LongToStringSerializer.class)
public @interface LongSerializeToString {

}
