package io.laokou.admin.infrastructure.common.user;
import io.laokou.admin.infrastructure.common.password.TokenUtil;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
public class SecurityUser {

    public static Long getUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader(Constant.USER_KEY_HEAD);
        if (StringUtils.isBlank(userIdHeader)) {
            String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);
            if (StringUtils.isBlank(authHeader)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            return TokenUtil.getUserId(authHeader);
        }
        return Long.valueOf(userIdHeader);
    }

    public static String getUsername(HttpServletRequest request) {
        String userNameHeader = request.getHeader(Constant.USERNAME_HEAD);
        if (StringUtils.isBlank(userNameHeader)) {
            String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);
            if (StringUtils.isBlank(authHeader)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            return TokenUtil.getUsername(authHeader);
        }
        return userNameHeader;
    }

}
