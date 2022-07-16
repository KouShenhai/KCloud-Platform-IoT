package io.laokou.datasource.feign.admin.fallback;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.datasource.feign.admin.AdminApiFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务降级
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Slf4j
@AllArgsConstructor
public class AdminApiFeignClientFallback implements AdminApiFeignClient {

    private Throwable throwable;

    @Override
    public HttpResultUtil<UserDetail> resource(String language, String Authorization, String uri, String method) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResultUtil<UserDetail>().error("服务调用失败，请联系管理员");
    }
}
