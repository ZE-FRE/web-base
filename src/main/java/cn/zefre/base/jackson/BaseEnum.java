package cn.zefre.base.jackson;

/**
 * 枚举类型接口
 *
 * @author pujian
 * @date 2023/1/7 16:35
 */
public interface BaseEnum<T> {

    /**
     * 枚举值代码
     *
     * @return T
     * @author pujian
     * @date 2023/1/9 10:59
     */
    T getCode();

    /**
     * 枚举值描述
     *
     * @return java.lang.String
     * @author pujian
     * @date 2023/1/9 10:59
     */
    String getDescription();

}
