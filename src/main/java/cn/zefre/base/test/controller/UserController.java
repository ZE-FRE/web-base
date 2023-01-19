package cn.zefre.base.test.controller;

import cn.zefre.base.test.controller.dto.UserDto;
import cn.zefre.base.web.annotation.RawResponse;
import cn.zefre.base.web.annotation.WrapResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pujian
 * @date 2022/1/27 13:53
 */
@Slf4j
@Validated
@WrapResponse
@RestController
@RequestMapping("/base/user")
public class UserController {

    @GetMapping("void")
    public void voidMethod() {
        log.info("void method has been invoked");
    }

    @GetMapping("getBoolean")
    public boolean getBoolean() {
        return true;
    }

    @GetMapping("getInteger")
    public int getInteger() {
        return 10;
    }

    @RawResponse
    @GetMapping("getIntegerWithRawType")
    public int getIntegerWithRawType() {
        return 10;
    }

    // @RequestMapping注解的consumes表示要求客户端请求头的Content-Type，而produces表示产生的，即返回给客户端的响应头的Content-Type
    @GetMapping(value = "getString", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getString() {
        return "A Letter";
    }

    @GetMapping("getById/{userId}")
    public UserDto getById(@Length(message = "用户id长度为1-4位", min = 1, max = 4)
                           @PathVariable String userId) {
        return new UserDto(userId, "张三");
    }

    @GetMapping("getByIdMap/{userId}")
    public Map<String, Object> getByIdMap(@Length(message = "用户id长度为1-4位", min = 1, max = 4)
                                          @PathVariable String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("username", "张三");
        return map;
    }

    @GetMapping("getByIdList")
    public List<String> getByIdList() {
        return Arrays.asList("zhagnsan", "lisi");
    }


    @GetMapping("testString")
    public String testString(@RequestParam(value = "orderNoParam", required = false) Long orderNo, String description) {
        System.out.println("进入");
        return "返回文本";
    }

    @PostMapping("testRequestBody")
    public UserDto testRequestBody(@RequestBody UserDto userDto) {
        System.out.println("处理业务");
        return new UserDto("123", "张三");
    }

    @PostMapping("testForm")
    public UserDto testForm(UserDto userDto) {
        System.out.println("处理表单业务");
        return new UserDto("456", "李四");
    }

}
