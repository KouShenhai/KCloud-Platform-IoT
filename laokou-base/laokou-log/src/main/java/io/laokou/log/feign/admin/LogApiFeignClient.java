/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.log.feign.admin;
import org.laokou.common.constant.ServiceConstant;
import org.laokou.common.dto.LoginLogDTO;
import org.laokou.common.dto.OperateLogDTO;
import org.laokou.common.utils.HttpResultUtil;
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
