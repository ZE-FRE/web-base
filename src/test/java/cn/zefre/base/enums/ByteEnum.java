package cn.zefre.base.enums;

import cn.zefre.base.jackson.BaseEnum;
import lombok.Getter;

/**
 * @author pujian
 * @date 2023/1/13 21:13
 */
@Getter
public enum ByteEnum implements BaseEnum<Byte> {
    BYTE_HELLO((byte) 1, "byte hello");

    private final Byte code;

    private final String description;

    ByteEnum(Byte code, String description) {
        this.code = code;
        this.description = description;
    }

}
