/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.rocketmq.consumer.feign.log;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.log.client.dto.AuditLogDTO;
import org.laokou.log.client.dto.LoginLogDTO;
import org.laokou.log.client.dto.OperateLogDTO;
import org.laokou.rocketmq.consumer.feign.log.factory.LogApiFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * @author laokou
 */
@FeignClient(name = ServiceConstant.LAOKOU_LOG,path = "/sys/log/api", fallbackFactory = LogApiFeignClientFallbackFactory.class)
@Service
public interface LogApiFeignClient {

    /**
     * 登陆日志
     * @param dto
     * @return
     */
    @PostMapping("/login")
    HttpResult<Boolean> login(@RequestBody LoginLogDTO dto);

    /**
     * 操作日志
     * @param dto
     * @return
     */
    @PostMapping("/operate")
    HttpResult<Boolean> operate(@RequestBody OperateLogDTO dto);

    /**
     * 审批日志
     * @param dto
     * @return
     */
    @PostMapping("/audit")
    HttpResult<Boolean> audit(@RequestBody AuditLogDTO dto);

}
