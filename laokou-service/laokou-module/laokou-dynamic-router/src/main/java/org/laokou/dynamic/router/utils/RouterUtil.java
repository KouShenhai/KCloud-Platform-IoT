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

package org.laokou.dynamic.router.utils;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.dynamic.router.RouteDefinition;
import org.laokou.freemarker.utils.TemplateUtil;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RouterUtil {
    private final Environment env;

   public void initRouter() throws IOException, TemplateException {
       String appId = env.getProperty("spring.application.name");
       Map<String,Object> dataMap = new HashMap<>(2);
       String name = appId.replace("laokou-","");
       dataMap.put("appId",appId);
       dataMap.put("name",name);
       byte[] bytes = new ClassPathResource("init_router.json").getInputStream().readAllBytes();
       String template = new String(bytes, StandardCharsets.UTF_8);
       String content = TemplateUtil.getContent(template, dataMap);
       RouteDefinition routeDefinition = JacksonUtil.toBean(content, RouteDefinition.class);
       log.info("获取路由信息：{}",JacksonUtil.toJsonStr(routeDefinition,true));
   }

}
