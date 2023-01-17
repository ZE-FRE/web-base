package cn.zefre.base.aop.log;

import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 后置处理器，对controller方法进行代理，打印日志
 *
 * @author pujian
 * @date 2023/1/17 16:40
 */
@Component
public class ControllerLogPostProcessor extends AbstractAdvisingBeanPostProcessor implements InitializingBean {

    @Resource
    private ControllerLogInterceptor controllerLogInterceptor;

    /**
     * 匹配{@link RequestMapping}注解标注的方法，对
     * {@link org.springframework.web.bind.annotation.GetMapping}
     * {@link org.springframework.web.bind.annotation.PostMapping}
     * {@link org.springframework.web.bind.annotation.PutMapping}
     * {@link org.springframework.web.bind.annotation.DeleteMapping}
     * 等注解也有效
     *
     * @author pujian
     * @date 2023/1/17 17:42
     */
    static class RequestMappingPointcut extends StaticMethodMatcherPointcut {
        @Override
        public boolean matches(Method method, Class<?> clazz) {
            MergedAnnotations annotations = MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.DIRECT);
            return annotations.isPresent(RequestMapping.class);
        }
    }

    @Override
    public void afterPropertiesSet() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new RequestMappingPointcut(), this.controllerLogInterceptor);
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE);
        this.advisor = advisor;
    }

}
