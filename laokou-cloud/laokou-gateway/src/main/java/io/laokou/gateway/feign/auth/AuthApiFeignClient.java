package io.laokou.gateway.feign.auth;
import io.laokou.common.constant.Constant;
import io.laokou.common.constant.ServiceConstant;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.gateway.config.FeignMultipartSupportConfig;
import io.laokou.gateway.feign.auth.factory.AuthApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_AUTH,configuration = FeignMultipartSupportConfig.class, fallbackFactory = AuthApiFeignClientFallbackFactory.class)
@Service
public interface AuthApiFeignClient {

    /**
     * 根据token获取用户信息
     * @param Authorization
     * @param uri
     * @param method
     * @param language
     * @return
     */
    @GetMapping("/sys/auth/api/resource")
    HttpResultUtil<UserDetail> resource(
                             @RequestHeader(HttpHeaders.ACCEPT_LANGUAGE) String language,
                             @RequestParam(Constant.AUTHORIZATION_HEADER) String Authorization,
                             @RequestParam(Constant.URI)String uri,
                             @RequestParam(Constant.METHOD)String method);
}
