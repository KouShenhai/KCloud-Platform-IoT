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
package org.laokou.common.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
public class AddressUtil {

    private static final String IP_URI = "https://ip.taobao.com/outGetIpInfo";

    private static final String ACCESS_KEY = "alibaba-inc";

    public static String getRealAddress(String ip) throws IOException {
        if (IpUtil.internalIp(ip)) {
            return "内网IP";
        }
        Map<String, String> params = new HashMap<>(2);
        params.put("ip", ip);
        params.put("accessKey", ACCESS_KEY);
        String ipJsonData = HttpUtil.transformerUnderHumpData(HttpUtil.doGet(IP_URI, params, new HashMap<>(0)));
        if (StringUtil.isNotEmpty(ipJsonData)) {
            JsonNode data = JacksonUtil.readTree(ipJsonData).get("data");
            return data.get("country").toString() + " " + data.get("city").toString();
        }
        return "XX XX";
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<>(2);
        params.put("ip", "218.190.235.177");
        params.put("accessKey", ACCESS_KEY);
        HttpUtil.transformerUnderHumpData(HttpUtil.doGet(IP_URI, params, new HashMap<>(0)));
    }

}
