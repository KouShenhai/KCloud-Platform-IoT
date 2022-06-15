package io.laokou.admin.infrastructure.common.user;
import io.laokou.admin.infrastructure.common.password.TokenUtil;
import io.laokou.common.constant.Constant;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
public class SecurityUser {

    public static Long getUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader(Constant.USER_KEY_HEAD);
        if (StringUtils.isBlank(userIdHeader)) {
            String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);
            if (StringUtils.isBlank(authHeader)) {
                return null;
            }
            return TokenUtil.getUserId(authHeader);
        }
        return Long.valueOf(userIdHeader);
    }

}
