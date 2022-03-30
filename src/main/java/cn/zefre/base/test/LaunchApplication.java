package cn.zefre.base.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author pujian
 * @date 2022/3/25 15:01
 */
@SpringBootApplication(scanBasePackages = "cn.zefre.base")
public class LaunchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaunchApplication.class, args);
    }
}
