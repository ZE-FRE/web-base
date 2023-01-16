package cn.zefre.base.jackson.deserializer;

import cn.zefre.base.jackson.BaseEnum;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

/**
 * 扩展{@link SimpleDeserializers}，主要是为了给{@link BaseEnum}的子类映射反序列化器
 *
 * jackson反序列化时，需要根据类型先从缓存中获取反序列化器，如果缓存中没有，则创建。
 * 创建时，如果发现是枚举类型，会先看有没有自定义的反序列化器，具体逻辑是：
 * 先依次调用{@link Deserializers#findEnumDeserializer(Class, DeserializationConfig, BeanDescription)}来尝试获取反序列化器
 * 如果没获取到，则创建一个jackson默认的枚举反序列化器，即：{@link com.fasterxml.jackson.databind.deser.std.EnumDeserializer}
 *
 * @author pujian
 * @date 2023/1/14 13:03
 */
public class BaseEnumDeserializers extends SimpleDeserializers {

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        JsonDeserializer<?> enumDeserializer = super.findEnumDeserializer(type, config, beanDesc);
        // 缓存中没有对应的反序列化器，生成一个并放入缓存中
        if (enumDeserializer == null && BaseEnum.class.isAssignableFrom(type)) {
            @SuppressWarnings("unchecked")
            Class<BaseEnum<?>> baseEnumClass = (Class<BaseEnum<?>>) type;
            BaseEnumDeserializer baseEnumDeserializer = new BaseEnumDeserializer(baseEnumClass);
            // 缓存起来
            addDeserializer(baseEnumClass, baseEnumDeserializer);
            return baseEnumDeserializer;
        }
        return enumDeserializer;
    }

}
