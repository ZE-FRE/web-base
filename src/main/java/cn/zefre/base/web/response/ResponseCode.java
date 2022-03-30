package cn.zefre.base.web.response;

/**
 * @author pujian
 * @date 2022/1/25 11:02
 */
public enum ResponseCode {

    OK(200, "操作成功"),

    ERROR(500, "服务器异常，请稍后再试"),

    UNAUTHORIZED(401, "身份信息未认证"),

    VALIDATION_EXCEPTION(400, "参数校验异常");


    /**
     * 响应状态码
     */
    private int code;

    /**
     * 描述
     */
    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
