package cn.zefre.base.jackson.deserializer;

import cn.zefre.base.jackson.BaseEnum;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link BaseEnum}反序列化器
 *
 * @author pujian
 * @date 2023/1/7 16:48
 */
public class BaseEnumDeserializer extends StdScalarDeserializer<BaseEnum<?>> {

    public BaseEnumDeserializer(Class<BaseEnum<?>> baseEnumClass) {
        super(baseEnumClass);
    }

    @Override
    public BaseEnum<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        // 反序列化目标枚举类
        Class<?> enumClass = handledType();
        JsonToken token = jsonParser.currentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return valueOf(jsonParser, jsonParser.getIntValue());
        } else if (token == JsonToken.VALUE_STRING || token == JsonToken.FIELD_NAME) {
            return valueOf(jsonParser, jsonParser.getText());
        } else {
            throw new JsonParseException(jsonParser, "无法将" + token + "类型的值反序列化到" + enumClass);
        }
    }

    private <T> BaseEnum<?> valueOf(JsonParser jsonParser, T value) throws JsonParseException {
        Class<?> enumClass = handledType();
        @SuppressWarnings("unchecked")
        Class<BaseEnum<?>> baseEnumClass = (Class<BaseEnum<?>>) enumClass;
        Method method;
        try {
            method = baseEnumClass.getDeclaredMethod("getCode");
        } catch (NoSuchMethodException e) {
            throw new JsonParseException(jsonParser, "反射获取" + enumClass + "#getCode()方法失败", e);
        }
        // 取得code的类型
        Class<?> codeType = method.getReturnType();
        Object code = null;
        if (codeType == Byte.class) {
            code = new Byte(value.toString());
        } else if (codeType == Integer.class) {
            code = new Integer(value.toString());
        } else if (codeType == String.class) {
            code = value.toString();
        }
        BaseEnum<?>[] baseEnums = baseEnumClass.getEnumConstants();
        for (BaseEnum<?> baseEnum : baseEnums) {
            if (baseEnum.getCode().equals(code)) {
                return baseEnum;
            }
        }
        List<String> existingCodes = Arrays.stream(baseEnums).map(BaseEnum::getCode).map(Object::toString).collect(Collectors.toList());
        String errorMsg = String.format("%s中没有%s，存在的值有%s", enumClass, value, existingCodes);
        throw new JsonParseException(jsonParser, errorMsg);
    }

}
