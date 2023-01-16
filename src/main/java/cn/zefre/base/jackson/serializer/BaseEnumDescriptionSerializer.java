package cn.zefre.base.jackson.serializer;

import cn.zefre.base.jackson.BaseEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;

/**
 * 序列化{@link BaseEnum#getDescription()}的值
 *
 * @author pujian
 * @date 2023/1/9 9:22
 */
public class BaseEnumDescriptionSerializer extends StdScalarSerializer<BaseEnum> {

    public BaseEnumDescriptionSerializer() {
        super(BaseEnum.class);
    }

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getDescription());
    }

}

