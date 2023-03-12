/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.log.utils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.ip.region.utils.AddressUtil;
import org.laokou.common.log.event.LoginLogEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@Slf4j
public class LoginLogUtil {

    @Async
    public void recordLogin(String username,String loginType, Integer status, String msg, HttpServletRequest request,Long tenantId) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
        String ip = IpUtil.getIpAddr(request);
        //获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        //获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        LoginLogEvent event = new LoginLogEvent(this);
        event.setLoginName(username);
        event.setRequestIp(ip);
        event.setRequestAddress(AddressUtil.getRealAddress(ip));
        event.setBrowser(browser);
        event.setOs(os);
        event.setMsg(msg);
        event.setLoginType(loginType);
        event.setRequestStatus(status);
        event.setTenantId(tenantId);
        SpringContextUtil.publishEvent(event);
    }

}
