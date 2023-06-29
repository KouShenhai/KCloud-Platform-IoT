package org.laokou.common.secret.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.secret.utils.SecretUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class ApiSecretAspect {

    private static final String NONCE = "nonce";
    private static final String TIMESTAMP = "timestamp";
    private static final String SIGN = "sign";
    public static final String APP_KEY = "app-key";

    public static final String APP_SECRET = "app-secret";

    @Before("@annotation(org.laokou.common.secret.annotation.ApiSecret)")
    public void doBefore() {
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        String nonce = request.getHeader(NONCE);
        String timestamp = request.getHeader(TIMESTAMP);
        String sign = request.getHeader(SIGN);
        String appKey = request.getHeader(APP_KEY);
        String appSecret = request.getHeader(APP_SECRET);
        MultiValueMap<String, String> multiValueMap = MapUtil.getParameters(request);
        SecretUtil.verification(appKey,appSecret,sign,nonce,timestamp, multiValueMap.toSingleValueMap());
    }

}
