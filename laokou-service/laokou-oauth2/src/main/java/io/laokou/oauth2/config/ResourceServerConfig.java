package io.laokou.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 资源配置
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/16 0016 下午 12:50
 */
@Configuration()
@EnableResourceServer()
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

     @Override
     public void configure(HttpSecurity http) throws Exception {
         http.requestMatchers()
                 .antMatchers("/userInfo")
                 .and()
                 .authorizeRequests().antMatchers().authenticated()
                 .and()
                 .authorizeRequests().antMatchers("/userInfo")
                 .access("#oauth2.hasScope('all')");
     }
}
