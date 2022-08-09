package io.laokou.auth.infrastructure.common.filter;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;
/**
 * authorize
 * @author Kou Shenhai
 */
@Data
public class AuthToken implements AuthenticationToken {

    private final String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
