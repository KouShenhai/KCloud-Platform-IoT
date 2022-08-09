package io.laokou.security.utils;

import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpContextUtil;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.security.feign.auth.AuthApiFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserDetailUtil {

    @Autowired
    private AuthApiFeignClient authApiFeignClient;

    public UserDetail getUserDetail(HttpServletRequest request) {
        String Authorization = getAuthorization(request);
        String language = HttpContextUtil.getLanguage();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        HttpResultUtil<UserDetail> result = authApiFeignClient.resource(language, Authorization, uri, method);
        if (!result.success()) {
            throw new CustomException(result.getCode(),result.getMsg());
        }
        return result.getData();
    }

    /**
     * 获取请求的token
     */
    private String getAuthorization(HttpServletRequest request){
        //从header中获取token
        String Authorization = request.getHeader(Constant.AUTHORIZATION_HEAD);
        //如果header中不存在Authorization，则从参数中获取Authorization
        if(StringUtils.isBlank(Authorization)){
            Authorization = request.getParameter(Constant.AUTHORIZATION_HEAD);
        }
        return Authorization;
    }

}
