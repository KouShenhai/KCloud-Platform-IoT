package io.laokou.admin.infrastructure.common.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * authorize
 * @author Kou Shenhai
 */
@Data
@AllArgsConstructor
public class Oauth2Token implements AuthenticationToken {

    @JsonProperty("Authorization")
    private String Authorization;

    @Override
    public Object getPrincipal() {
        return Authorization;
    }

    @Override
    public Object getCredentials() {
        return Authorization;
    }

}
