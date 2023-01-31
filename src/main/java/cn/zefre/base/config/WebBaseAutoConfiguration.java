package cn.zefre.base.config;

import cn.zefre.base.log.RequestResponseLoggingFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author pujian
 * @date 2022/9/18 16:02
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = "cn.zefre.base")
@EnableConfigurationProperties(BaseProperties.class)
public class WebBaseAutoConfiguration {

    public final BaseProperties baseProperties;

    public WebBaseAutoConfiguration(BaseProperties commonProperties) {
        this.baseProperties = commonProperties;
    }

    @Bean
    public RequestResponseLoggingFilter requestResponseLoggingFilter() {
        return new RequestResponseLoggingFilter(this.baseProperties.getMvcLog());
    }

}
