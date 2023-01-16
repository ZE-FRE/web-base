package cn.zefre.base;

import cn.zefre.base.enums.ByteEnum;
import cn.zefre.base.enums.IntegerEnum;
import cn.zefre.base.enums.StringEnum;
import cn.zefre.base.jackson.BaseEnum;
import cn.zefre.base.jackson.deserializer.BaseEnumDeserializers;
import cn.zefre.base.jackson.serializer.BaseEnumCodeSerializer;
import cn.zefre.base.jackson.serializer.BaseEnumDescriptionSerializer;
import cn.zefre.base.jackson.serializer.BaseEnumObjectSerializer;
import cn.zefre.base.jackson.serializer.BaseEnumSerializers;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author pujian
 * @date 2023/1/13 21:19
 */
public class EnumTest {

    @Test
    public void testDefaultSerialization() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 注册序列化器
        simpleModule.setSerializers(new BaseEnumSerializers());
        objectMapper.registerModule(simpleModule);
        assertEquals(ByteEnum.BYTE_HELLO.getDescription(), objectMapper.valueToTree(ByteEnum.BYTE_HELLO).asText());

        // 已经缓存了ByteEnum的序列化器了，这里再注册新的序列化器进去已经没用了
        SimpleModule codeModule = new SimpleModule();
        codeModule.addSerializer(ByteEnum.class, new BaseEnumCodeSerializer());
        objectMapper.registerModule(codeModule);
        assertEquals(ByteEnum.BYTE_HELLO.getDescription(), objectMapper.valueToTree(ByteEnum.BYTE_HELLO).asText());
    }

    @Test
    public void testSerializeAllField() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册序列化器，此种方式只会注册一个序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BaseEnum.class, new BaseEnumObjectSerializer());
        objectMapper.registerModule(simpleModule);

        System.out.println(objectMapper.writeValueAsString(ByteEnum.BYTE_HELLO));
        System.out.println(objectMapper.writeValueAsString(IntegerEnum.INTEGER_HELLO));
        System.out.println(objectMapper.writeValueAsString(StringEnum.STRING_HELLO));
    }



    @Test
    public void testDeserialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册反序列化器，此种方式会在运行时为当前枚举类(BaseEnum的子类)生成一个反序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setDeserializers(new BaseEnumDeserializers());
        objectMapper.registerModule(simpleModule);

        assertEquals(ByteEnum.BYTE_HELLO, objectMapper.convertValue(1, ByteEnum.class));
        assertEquals(IntegerEnum.INTEGER_HELLO, objectMapper.convertValue(1, IntegerEnum.class));
        assertEquals(StringEnum.STRING_HELLO, objectMapper.convertValue("1", StringEnum.class));
        assertThrows(JsonParseException.class, () -> objectMapper.readValue("\"unknown\"", StringEnum.class));
        assertThrows(JsonParseException.class, () -> objectMapper.readValue("[]", StringEnum.class));
    }




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnumInObject {
        private ByteEnum byteEnum;

        private IntegerEnum integerEnum;

        private StringEnum stringEnum;
    }
    @Test
    public void testSerializeInObj() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 注册序列化器
        simpleModule.addSerializer(BaseEnum.class, new BaseEnumDescriptionSerializer());
        objectMapper.registerModule(simpleModule);

        EnumInObject enumInObject = new EnumInObject(ByteEnum.BYTE_HELLO, IntegerEnum.INTEGER_HELLO, StringEnum.STRING_HELLO);
        JsonNode jsonNode = objectMapper.valueToTree(enumInObject);
        assertEquals(ByteEnum.BYTE_HELLO.getDescription(), jsonNode.get("byteEnum").asText());
        assertEquals(IntegerEnum.INTEGER_HELLO.getDescription(), jsonNode.get("integerEnum").asText());
        assertEquals(StringEnum.STRING_HELLO.getDescription(), jsonNode.get("stringEnum").asText());
        // System.out.println(objectMapper.writeValueAsString(enumInObject));
    }
    @Test
    public void testDeserializeInObj() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 注册反序列化器
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setDeserializers(new BaseEnumDeserializers());
        objectMapper.registerModule(simpleModule);

        EnumInObject enumInObject = objectMapper.readValue("{\"byteEnum\":1,\"integerEnum\":1,\"stringEnum\":1}", EnumInObject.class);
        assertEquals(new EnumInObject(ByteEnum.BYTE_HELLO, IntegerEnum.INTEGER_HELLO, StringEnum.STRING_HELLO), enumInObject);
        System.out.println(enumInObject);
    }


    @Data
    @AllArgsConstructor
    static class EnumAnnotatedWithJsonSerialize {
        @JsonSerialize(using = BaseEnumCodeSerializer.class)
        private ByteEnum byteEnum;

        @JsonSerialize(using = BaseEnumObjectSerializer.class)
        private ByteEnum byteEnum2;
    }

    /**
     * 测试@JsonSerialize注解标注的序列化器优先级更高
     *
     * @author pujian
     * @date 2023/1/14 21:58
     */
    @Test
    public void testSerializationOverriding() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 注册序列化器
        simpleModule.addSerializer(BaseEnum.class, new BaseEnumDescriptionSerializer());
        objectMapper.registerModule(simpleModule);

        JsonNode jsonNode = objectMapper.valueToTree(new EnumAnnotatedWithJsonSerialize(ByteEnum.BYTE_HELLO, ByteEnum.BYTE_HELLO));
        assertEquals(ByteEnum.BYTE_HELLO.getCode(), (byte) jsonNode.get("byteEnum").intValue());
        assertTrue(jsonNode.get("byteEnum2").isObject());
        // System.out.println(objectMapper.writeValueAsString(new EnumUsingAnn(ByteEnum.BYTE_HELLO, ByteEnum.BYTE_HELLO)));
        assertEquals(ByteEnum.BYTE_HELLO.getDescription(), objectMapper.valueToTree(ByteEnum.BYTE_HELLO).asText());
    }




    enum PassengerType {
        ADULT
    }
    @Test
    public void testEnum() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(PassengerType.ADULT, objectMapper.readValue("\"ADULT\"", PassengerType.class));
        assertEquals(PassengerType.ADULT, objectMapper.convertValue("ADULT", PassengerType.class));
        assertEquals(PassengerType.ADULT.name(), objectMapper.valueToTree(PassengerType.ADULT).asText());
    }

}
