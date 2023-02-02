package cn.zefre.base.jackson;

import cn.zefre.base.jackson.serializer.LongToStringSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author pujian
 * @date 2023/2/2 9:41
 */
public class LongToStringTest {

    @Test
    public void testLongToString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 注册全局Long.class、long.class序列化器
        simpleModule.addSerializer(Long.class, new LongToStringSerializer());
        simpleModule.addSerializer(long.class, new LongToStringSerializer());
        objectMapper.registerModule(simpleModule);
        System.out.println(objectMapper.writeValueAsString(new Long(100)));
        System.out.println(objectMapper.writeValueAsString(100L));
    }


    static class LongSerializer extends JsonSerializer<Long> {
        private NumberSerializer numberSerializer = NumberSerializer.instance;
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            numberSerializer.serialize(value, gen, provider);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LongWrapper {
        @JsonSerialize(using = LongSerializer.class)
        private Long longWrap;
        private long longPrimitive;
    }

    @Test
    public void testPriority() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 注册全局Long.class、long.class序列化器
        simpleModule.addSerializer(Long.class, new LongToStringSerializer());
        simpleModule.addSerializer(long.class, new LongToStringSerializer());
        objectMapper.registerModule(simpleModule);
        System.out.println(objectMapper.writeValueAsString(new LongWrapper(100L, 100L)));
    }

}
