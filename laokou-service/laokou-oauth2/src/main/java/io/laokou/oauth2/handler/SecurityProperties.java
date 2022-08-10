package io.laokou.oauth2.handler;

import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/10 0010 下午 3:19
 */
@Data
public class SecurityProperties {

    private String clientId;
    private String type;
    private String scopes;
    private String secret;
    private String redirectUri;

}
