/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.PropertiesConstants.ROUTER_AUTH_PREFIX;
import static org.laokou.common.i18n.common.StringConstants.ANNOTATION;
import static org.laokou.common.i18n.common.SysConstants.DEFAULT_PASSWORD;
import static org.laokou.common.i18n.common.SysConstants.DEFAULT_USERNAME;

/**
 * 网关扩展属性配置.
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = ROUTER_AUTH_PREFIX)
public class GatewayExtProperties {

    private String type = ANNOTATION;
    private String username = DEFAULT_USERNAME;
    private String password = DEFAULT_PASSWORD;

}
