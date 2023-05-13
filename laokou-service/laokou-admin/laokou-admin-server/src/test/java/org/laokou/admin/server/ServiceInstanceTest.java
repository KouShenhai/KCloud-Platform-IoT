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

package org.laokou.admin.server;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.nacos.utils.ServiceUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.test.context.TestConstructor;
/**
 * @author laokou
 */
@SpringBootTest
@Slf4j
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ServiceInstanceTest {

    private final ServiceUtil serviceUtil;

    @Test
    @SneakyThrows
    void test() {
        ServiceInstance serviceInstance = serviceUtil.getServiceInstance(ServiceConstant.LAOKOU_FLOWABLE);
        log.info("data -> ：{}", JacksonUtil.toJsonStr(serviceInstance));
    }

}
