package io.laokou.security.utils;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.SecurityUser;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpContextUtil;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.feign.auth.AuthApiFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
@Component
public class UserDetailUtil {

    @Autowired
    private AuthApiFeignClient authApiFeignClient;

    public UserDetail getUserDetail(HttpServletRequest request) {
        String Authorization = SecurityUser.getAuthorization(request);
        String language = HttpContextUtil.getLanguage();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        HttpResultUtil<UserDetail> result = authApiFeignClient.resource(language, Authorization, uri, method);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        return result.getData();
    }

}
