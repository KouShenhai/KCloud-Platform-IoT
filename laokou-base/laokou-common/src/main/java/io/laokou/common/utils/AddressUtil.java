package io.laokou.common.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class AddressUtil {

    private static final String IP_URI = "http://ip.taobao.com/outGetIpInfo";

    private static final String ACCESS_KEY = "alibaba-inc";

    public static String getRealAddress(String ip) throws IOException {
        if (IpUtil.internalIp(ip)) {
            return "内网IP";
        }
        Map<String,String> params = new HashMap<>(2);
        params.put("ip",ip);
        params.put("accessKey",ACCESS_KEY);
        String ipJsonData = HttpUtil.transformerUnderHumpData(HttpUtil.doGet(IP_URI,params,new HashMap<>(0)));
        if (StringUtils.isNotBlank(ipJsonData)) {
            Map<String,String> map = JacksonUtil.toMap(ipJsonData,String.class,String.class);
            Map<String, String> dataMap = JacksonUtil.toMap(map.get("data"), String.class, String.class);
            return dataMap.get("country") + " " + dataMap.get("city");
        }
        return "XX XX";
    }

}
