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
package org.laokou.auth.infrastructure.component;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;
/**
 * sso
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/13 0013 下午 2:34
 */
@Component
@ConfigurationProperties(prefix = "auth")
@RefreshScope
@Data
@Slf4j
public class AuthHandler {

    private List<AuthProperties> uris;

    @Resource
    private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @ApolloConfigChangeListener
    private void changeHandler(ConfigChangeEvent event) {
        refreshScope.refreshAll();
        log.info("apollo动态拉取配置...");
    }

}
