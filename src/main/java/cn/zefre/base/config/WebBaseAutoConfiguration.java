package cn.zefre.base.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author pujian
 * @date 2022/9/18 16:02
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "cn.zefre.base")
public class WebBaseAutoConfiguration {

}
