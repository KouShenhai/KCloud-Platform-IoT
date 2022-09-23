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
package io.laokou.log.feign.admin.fallback;
import org.laokou.common.dto.LoginLogDTO;
import org.laokou.common.dto.OperateLogDTO;
import org.laokou.common.utils.HttpResultUtil;
import io.laokou.log.feign.admin.LogApiFeignClient;
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
public class LogApiFeignClientFallback implements LogApiFeignClient {

    private final Throwable throwable;

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
