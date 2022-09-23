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
package org.laokou.register.listen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Eureka 监控
 * @author Kou Shenhai
 * @version 1.0
 * @date 2021/11/18 0018 下午 8:32
 */
@Component
public class EurekaStatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EurekaStatusListener.class);

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        LOGGER.info("ServerId:{}",event.getServerId());
        LOGGER.info("服务下线：{}",event.getAppName());
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        LOGGER.info("注册服务:{}",event.getInstanceInfo().getAppName());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        LOGGER.info("ServerId:{}",event.getServerId());
        LOGGER.info("服务续约：{}",event.getAppName());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        LOGGER.info("注册中心 启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        LOGGER.info("Eureka Server 启动");
    }

}
