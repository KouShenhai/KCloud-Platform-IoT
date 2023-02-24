/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.rocketmq.consumer;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.swagger.config.CorsConfig;
import org.laokou.rocketmq.client.constant.RocketmqConstant;
import org.laokou.rocketmq.consumer.message.ConsumerMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import java.util.function.Consumer;
/**
 * @author laokou
 */
@SpringBootApplication(scanBasePackages = {"org.laokou.openfeign","org.laokou.sentinel","org.laokou.common.core","org.laokou.rocketmq.consumer"})
@EnableDiscoveryClient
@EnableFeignClients
@Import({CorsConfig.class})
@EnableEncryptableProperties
public class RocketmqConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqConsumerApplication.class, args);
    }

    @Bean
    Consumer<Message<String>> consumer() {
        return msg -> {
            ConsumerMessage message = SpringContextUtil.getBean(msg.getHeaders().get(RocketmqConstant.TAG,String.class), ConsumerMessage.class);
            message.receiveMessage(msg.getPayload());
        };
    }

}
