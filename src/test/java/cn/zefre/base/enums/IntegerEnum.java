package cn.zefre.base.enums;

import cn.zefre.base.jackson.BaseEnum;
import lombok.Getter;

/**
 * @author pujian
 * @date 2023/1/14 13:57
 */
@Getter
public enum IntegerEnum implements BaseEnum<Integer> {
    INTEGER_HELLO(1, "integer hello");

    private final Integer code;

    private final String description;

    IntegerEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

}
