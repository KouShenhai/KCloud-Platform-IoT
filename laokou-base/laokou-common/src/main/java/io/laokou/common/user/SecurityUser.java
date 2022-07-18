package io.laokou.common.user;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.common.utils.TokenUtil;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
public class SecurityUser {

    public static Long getUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader(Constant.USER_KEY_HEAD);
        if (StringUtils.isBlank(userIdHeader)) {
            String authHeader = getAuthorization(request);
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
            String authHeader = getAuthorization(request);
            if (StringUtils.isBlank(authHeader)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            return TokenUtil.getUsername(authHeader);
        }
        return userNameHeader;
    }

    /**
     * 获取请求的token
     */
    private static String getAuthorization(HttpServletRequest request){
        //从header中获取token
        String Authorization = request.getHeader(Constant.AUTHORIZATION_HEADER);
        //如果header中不存在Authorization，则从参数中获取Authorization
        if(org.apache.commons.lang3.StringUtils.isBlank(Authorization)){
            Authorization = request.getParameter(Constant.AUTHORIZATION_HEADER);
        }
        return Authorization;
    }

}
