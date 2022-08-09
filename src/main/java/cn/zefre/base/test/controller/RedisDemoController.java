package cn.zefre.base.test.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pujian
 * @date 2022/8/2 11:01
 */
@RestController
@RequestMapping("redis")
public class RedisDemoController {

    @Component("myKeyGenerator")
    static class MyKeyGenerator implements KeyGenerator {

        @Override
        public Object generate(Object o, Method method, Object... params) {
            if (params.length == 0) {
                return "";
            } else if (params.length == 1) {
                return params;
            } else {
                return Arrays.stream(params).map(String::valueOf).collect(Collectors.joining("-"));
            }
        }
    }

    @Cacheable(cacheNames = "usernameList")
    @GetMapping("usernameList")
    public List<String> usernameList() {
        System.out.println("缓存失效");
        List<String> names = new ArrayList<>();
        names.add("张三");
        names.add("张三");
        names.add("张三");
        return names;
    }

    @Cacheable(cacheNames = "username")
    @GetMapping("username")
    public String username(String id, String username) {
        return "张三";
    }

}
