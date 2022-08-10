package io.laokou.oauth2.config;
import io.laokou.oauth2.constant.OauthConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import javax.sql.DataSource;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/15 0015 下午 4:37
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(OauthConstant.SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(OauthConstant.FIND_STATEMENT);
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 授权码管理
     */
    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //允许表单验证
        security.allowFormAuthenticationForClients();
        //匿名访问/oauth/token_key
        security.tokenKeyAccess("permitAll()");
        //认证后可访问/oauth/check_token
        security.checkTokenAccess("isAuthenticated()");
        //加密
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //登录或者鉴权失败时的返回信息
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE);
        endpoints.exceptionTranslator(webResponseExceptionTranslator);
        //授权码管理，授权码放在oauth_code表里面
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices());
    }
}
