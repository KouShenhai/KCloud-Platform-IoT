package org.laokou.auth.event.handler;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.ip.region.utils.AddressUtil;
import org.laokou.common.log.event.LoginLogEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class LoginLogHandler {

    public LoginLogEvent handleEvent(String username, String loginType, Integer status, String msg, HttpServletRequest request, Long tenantId) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
        // IP地址
        String ip = IpUtil.getIpAddr(request);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
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
        return event;
    }

}
