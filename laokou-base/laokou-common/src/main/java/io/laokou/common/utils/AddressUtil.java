package io.laokou.common.utils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
        String ipJsonData = HttpUtil.transformerUnderHumpData(HttpUtil.doGet(IP_URI,params));
        if (StringUtils.isNotBlank(ipJsonData)) {
            JSONObject jsonObject = JSONObject.parseObject(ipJsonData).getJSONObject("data");
            return jsonObject.getString("country") + " " + jsonObject.getString("city");
        }
        return "XX XX";
    }

}
