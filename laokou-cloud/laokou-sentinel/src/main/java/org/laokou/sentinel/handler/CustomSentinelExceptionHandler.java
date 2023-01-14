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
package org.laokou.sentinel.handler;
import cn.hutool.http.HttpStatus;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.utils.JacksonUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Component
public class CustomSentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, BlockException e) throws Exception {
        Map<String,Object> dataMap = new HashMap<>(2);
        dataMap.put("code",429);
        dataMap.put("msg","服务已被限流，请稍后再试");
        response.setStatus(HttpStatus.HTTP_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JacksonUtil.toJsonStr(dataMap));
        writer.flush();
        writer.close();
    }
}
