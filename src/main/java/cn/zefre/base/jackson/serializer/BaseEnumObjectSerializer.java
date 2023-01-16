package cn.zefre.base.jackson.serializer;

import cn.zefre.base.jackson.BaseEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;

/**
 * 把{@link BaseEnum}序列化成对象
 *
 * @author pujian
 * @date 2023/1/9 9:22
 */
public class BaseEnumObjectSerializer extends StdScalarSerializer<BaseEnum> {

    public BaseEnumObjectSerializer() {
        super(BaseEnum.class);
    }

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        Object code = value.getCode();
        Class<?> codeType = code.getClass();
        if (codeType == Byte.class || codeType == Integer.class) {
            gen.writeNumberField("code", new Integer(code.toString()));
        } else if (codeType == String.class) {
            gen.writeStringField("code", code.toString());
        }
        gen.writeStringField("description", value.getDescription());
    }

}
