package io.laokou.oauth2.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/4/15 0015 下午 4:37
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${security.auth.client_id}")
    private String CLIENT_ID;

    @Value("${security.auth.scopes}")
    private String SCOPES;

    @Value("${security.auth.secret}")
    private String SECRET;

    @Value("${security.auth.type}")
    private String TYPE;

    @Value("${security.auth.redirect_uri}")
    private String REDIRECT_URI;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebResponseExceptionTranslator<OAuth2Exception> webResponseExceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //in-memory存储
        clients.inMemory()
                .withClient(CLIENT_ID)
                //授权类型
                .authorizedGrantTypes(TYPE)
                .scopes(SCOPES)
                .secret(SECRET)
                .redirectUris(REDIRECT_URI)
                .autoApprove(true);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //允许表单验证
        security.allowFormAuthenticationForClients();
        //匿名访问/oauth/token_key
        security.tokenKeyAccess("permitAll()");
        //认证后可访问/oauth/check_token
        security.checkTokenAccess("isAuthenticated()");
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        //登录或者鉴权失败时的返回信息
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE);
        endpoints.exceptionTranslator(webResponseExceptionTranslator);
    }
}
