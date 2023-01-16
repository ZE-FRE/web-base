package cn.zefre.base.enums;

import cn.zefre.base.jackson.BaseEnum;
import lombok.Getter;

/**
 * @author pujian
 * @date 2023/1/14 13:09
 */
@Getter
public enum StringEnum implements BaseEnum<String> {

    STRING_HELLO("1", "string hello");

    private final String code;

    private final String description;

    StringEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
