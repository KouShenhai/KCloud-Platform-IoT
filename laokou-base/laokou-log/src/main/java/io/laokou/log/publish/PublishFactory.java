package io.laokou.log.publish;

import com.google.common.net.HttpHeaders;
import eu.bitwalker.useragentutils.UserAgent;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.utils.AddressUtil;
import io.laokou.common.utils.HttpContextUtil;
import io.laokou.common.utils.IpUtil;
import io.laokou.common.utils.SpringContextUtil;
import io.laokou.log.event.LoginLogEvent;

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
