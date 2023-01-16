package cn.zefre.base.jackson.serializer;

import cn.zefre.base.jackson.BaseEnum;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

/**
 * @author pujian
 * @date 2023/1/14 16:03
 */
public class BaseEnumSerializers extends SimpleSerializers {

    @Override
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        JsonSerializer<?> serializer = super.findSerializer(config, type, beanDesc);
        if (serializer == null && BaseEnum.class.isAssignableFrom(type.getRawClass())) {
            // 序列化BaseEnum时，默认序列化description
            serializer = new BaseEnumDescriptionSerializer();
            addSerializer(serializer);
        }
        return serializer;
    }

}
