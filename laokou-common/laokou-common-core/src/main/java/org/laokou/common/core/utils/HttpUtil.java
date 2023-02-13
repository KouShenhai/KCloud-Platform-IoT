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
package org.laokou.common.core.utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.laokou.common.core.constant.Constant;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * http工具类
 * @author laokou
 */
@Slf4j
public class HttpUtil {

    private static final Pattern LINE_PATTERN = Pattern.compile("_(\\w)");

    public static String doGet(String url, Map<String, String> params,Map<String, String> headers) throws IOException {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            //创建uri
            URIBuilder builder = new URIBuilder(url);
            if (!params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            //创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    httpGet.addHeader(e.getKey(), e.getValue());
                }
            }
            List<NameValuePair> paramList = new ArrayList<>();
            RequestBuilder requestBuilder = RequestBuilder.get().setUri(new URI(url));
            requestBuilder.setEntity(new UrlEncodedFormEntity(paramList, Consts.UTF_8));
            httpGet.setHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
            httpGet.setHeader(new BasicHeader("Accept", "*/*;charset=utf-8"));
            //执行请求
            response = httpClient.execute(httpGet);
            //判断返回状态是否是200
            if (response.getStatusLine().getStatusCode() == Constant.SUCCESS) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("调用失败，错误信息:{}",e);
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        log.info("打印：{}",resultString);
        return resultString;
    }

    public static String doPost(String url,Map<String, String> params,Map<String,String> headers) throws IOException {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            //创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> e : headers.entrySet()) {
                    httpPost.addHeader(e.getKey(), e.getValue());
                }
            }
            //创建参数列表
            if (params != null && params.size() > 0) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                //模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, StandardCharsets.UTF_8);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;application/json;charset=UTF-8");
                httpPost.setHeader(new BasicHeader("Accept", "*/*;charset=utf-8"));
                //执行http请求
                response = httpClient.execute(httpPost);
                //判断返回状态是否是200
                if (response.getStatusLine().getStatusCode() == Constant.SUCCESS) {
                    resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                }
            }
        }catch (Exception e) {
            log.error("接口调用失败:{}",e);
        }finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        log.info("打印：{}",resultString);
        return resultString;
    }

    /**
     * 转换为驼峰json字符串
     * @param data
     * @return
     */
    public static String transformerUnderHumpData(String data) {
        data = data.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(data);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
