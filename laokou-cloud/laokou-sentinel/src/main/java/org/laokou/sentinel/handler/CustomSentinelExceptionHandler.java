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
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.common.i18n.core.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
/**
 * @author laokou
 */
@Component
@Slf4j
public class CustomSentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, BlockException e) throws Exception {
        if (e instanceof FlowException) {
            log.info("接口被限流");
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.write(JacksonUtil.toJsonStr(new HttpResult().error(StatusCode.API_BLOCK_REQUEST)));
            writer.flush();
            writer.close();
        } else if (e instanceof DegradeException) {
            log.info("接口被降级");
        }
    }
}
