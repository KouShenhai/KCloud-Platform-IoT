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
package io.laokou.log.publish;

import eu.bitwalker.useragentutils.UserAgent;
import org.laokou.common.dto.LoginLogDTO;
import org.laokou.common.utils.AddressUtil;
import org.laokou.common.utils.HttpContextUtil;
import org.laokou.common.utils.IpUtil;
import org.laokou.common.utils.SpringContextUtil;
import io.laokou.log.event.LoginLogEvent;
import org.apache.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class PublishFactory {

    public static void recordLogin(String username,Integer status,String msg) throws IOException {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
        String ip = IpUtil.getIpAddr(request);
        //获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        //获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        LoginLogDTO dto = new LoginLogDTO();
        dto.setLoginName(username);
        dto.setRequestIp(ip);
        dto.setRequestAddress(AddressUtil.getRealAddress(ip));
        dto.setBrowser(browser);
        dto.setOs(os);
        dto.setMsg(msg);
        dto.setRequestStatus(status);
        //发布事件
        SpringContextUtil.publishEvent(new LoginLogEvent(dto));
    }

}
