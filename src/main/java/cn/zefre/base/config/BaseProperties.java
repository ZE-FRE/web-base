package cn.zefre.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pujian
 * @date 2023/1/31 15:13
 */
@Data
@ConfigurationProperties(prefix = "base")
public class BaseProperties {

    private MvcLog mvcLog = new MvcLog();

    @Data
    public static class MvcLog {
        /**
         * 是否开启打印mvc请求响应日志，默认开启
         */
        private boolean enable = true;
        /**
         * 是否打印请求日志，默认开启
         */
        private boolean logRequest = true;

        /**
         * 是否打印响应日志，默认关闭
         */
        private boolean logResponse = false;
    }

}
