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
package io.laokou.log.listener;
import org.laokou.common.dto.LoginLogDTO;
import org.laokou.common.dto.OperateLogDTO;
import io.laokou.log.event.LoginLogEvent;
import io.laokou.log.event.OperateLogEvent;
import io.laokou.log.feign.admin.LogApiFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@ConditionalOnWebApplication
public class LogListener {

    @Autowired
    private LogApiFeignClient logApiFeignClient;

    @Order
    @EventListener(OperateLogEvent.class)
    public void listenOperateLog(OperateLogEvent event) {
        OperateLogDTO dto = (OperateLogDTO) event.getSource();
        logApiFeignClient.insertOperateLog(dto);
    }

    @Order
    @EventListener(LoginLogEvent.class)
    public void listenLoginLog(LoginLogEvent event) {
        LoginLogDTO dto = (LoginLogDTO) event.getSource();
        logApiFeignClient.insertLoginLog(dto);
    }

}
