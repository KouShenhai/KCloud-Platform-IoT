package io.laokou.log.feign.admin.fallback;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.feign.admin.AdminApiFeignClient;
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
    public HttpResultUtil<Boolean> insertOperateLog(OperateLogDTO dto) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResultUtil<Boolean>().error("服务调用失败，请联系管理员");
    }

    @Override
    public HttpResultUtil<Boolean> insertLoginLog(LoginLogDTO dto) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResultUtil<Boolean>().error("服务调用失败，请联系管理员");
    }
}
