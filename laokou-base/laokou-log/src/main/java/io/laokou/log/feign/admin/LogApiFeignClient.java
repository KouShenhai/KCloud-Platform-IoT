package io.laokou.log.feign.admin;
import io.laokou.common.constant.ServiceConstant;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.utils.HttpResultUtil;
import io.laokou.log.feign.admin.factory.LogApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author Kou Shenhai
 */
@FeignClient(name = ServiceConstant.LAOKOU_ADMIN,path = "/sys/log/api", fallbackFactory = LogApiFeignClientFallbackFactory.class)
@Service
public interface LogApiFeignClient {

    @PostMapping(value = "/operate/insert",consumes = MediaType.APPLICATION_JSON_VALUE)
    HttpResultUtil<Boolean> insertOperateLog(@RequestBody OperateLogDTO dto);

    @PostMapping(value = "/login/insert",consumes = MediaType.APPLICATION_JSON_VALUE)
    HttpResultUtil<Boolean> insertLoginLog(@RequestBody LoginLogDTO dto);
}
