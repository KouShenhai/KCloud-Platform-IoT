package io.laokou.security.config;
import feign.RequestInterceptor;
import io.laokou.common.constant.Constant;
import io.laokou.common.utils.HttpContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.http.HttpServletRequest;
/**
 * @author  Kou Shenhai
 */
@Configuration
public class FeignMultipartSupportConfig {

    @Bean
    public feign.Logger.Level multipartLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return requestTemplate -> {
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            requestTemplate.header(Constant.AUTHORIZATION_HEADER,request.getHeader(Constant.AUTHORIZATION_HEADER));
        };
    }

}
