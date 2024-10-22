/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.netty.config;

import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

// @formatter:off
/**
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnProperty(havingValue = "true", matchIfMissing = true, prefix = "spring.tcp-server", name = "enabled")
public class TcpServerAutoConfig {

    @Bean(name = "tcpServer", initMethod = "start", destroyMethod = "stop")
    public Server tcpServer(SpringTcpServerProperties springTcpServerProperties,
                            ChannelHandler tcpServerChannelInitializer) {
        return new TcpServer(springTcpServerProperties.getIp(), springTcpServerProperties.getPort(),
                tcpServerChannelInitializer, springTcpServerProperties.getBossCoreSize(),
                springTcpServerProperties.getWorkerCoreSize());
    }

    @Bean
    public EventExecutorGroup tcpServerEventExecutorGroup(SpringTcpServerProperties springTcpServerProperties) {
        return new UnorderedThreadPoolEventExecutor(springTcpServerProperties.getCorePoolSize(), new DefaultThreadFactory("tcpServerHandler"));
    }

}
// @formatter:on
