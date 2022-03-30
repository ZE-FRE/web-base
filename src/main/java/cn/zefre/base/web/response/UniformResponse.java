package cn.zefre.base.web.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pujian
 * @date 2022/1/25 11:02
 */
@Getter
@Setter
@NoArgsConstructor
public class UniformResponse {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    public UniformResponse(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getDescription();
    }

    /**
     * 响应成功
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse ok() {
        return respond(ResponseCode.OK);
    }

    /**
     * 响应成功，自定义message
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse ok(String message) {
        UniformResponse uniformResponse = ok();
        uniformResponse.setMessage(message);
        return uniformResponse;
    }

    /**
     * 响应成功，自定义data
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse ok(Object data) {
        UniformResponse uniformResponse = ok();
        uniformResponse.setData(data);
        return uniformResponse;
    }

    /**
     * 响应成功，自定义message以及data
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse ok(String message, Object data) {
        UniformResponse uniformResponse = ok(message);
        uniformResponse.setData(data);
        return uniformResponse;
    }

    /**
     * 响应错误
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse error() {
        return respond(ResponseCode.ERROR);
    }

    /**
     * 响应错误，自定义message
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse error(String message) {
        UniformResponse uniformResponse = error();
        uniformResponse.setMessage(message);
        return uniformResponse;
    }

    /**
     * 响应未认证
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse unauthorized() {
        return respond(ResponseCode.UNAUTHORIZED);
    }

    /**
     * 响应参数验证错误
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse validationFailed(String message) {
        return respond(ResponseCode.VALIDATION_EXCEPTION, message);
    }

    /**
     * 生成响应对象
     *
     * @author pujian
     * @date 2021/10/20 11:00
     */
    public static UniformResponse respond(ResponseCode responseCode) {
        return new UniformResponse(responseCode);
    }

    public static UniformResponse respond(ResponseCode responseCode, String message) {
        UniformResponse uniformResponse = new UniformResponse(responseCode);
        uniformResponse.setMessage(message);
        return uniformResponse;
    }

}
