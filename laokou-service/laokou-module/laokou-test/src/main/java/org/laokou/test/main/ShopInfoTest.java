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
package org.laokou.test.main;
import org.laokou.common.core.utils.HttpUtil;
import org.laokou.common.core.utils.JacksonUtil;
import java.util.HashMap;
import java.util.Map;
/**
 * @author laokou
 */
public class ShopInfoTest {

    /**
     * 获取token链接
     */
    private static final String TOKEN_URI = "";

    /**
     * 商品列表链接
     */
    private static final String SHOP_INFO_URI = "";

    public static void main(String[] args) {
        // 1.获取token
        // 应用私钥
        String appKey = "";
        // 应用密钥
        String appSecret = "";
        // 客户端编号
        String clientId = "";
        // 获取accessToken
        String accessToken = getAccessToken(appKey,appSecret,clientId);
        // 2.获取商品信息
        // 名牌名称，模糊查询
        String brandName = "";
        // 页码
        int page = 1;
        // 页数
        int size = 10;
        String shopInfoList = getShopInfoList(accessToken, page, size,brandName);
        // 3.打印商品列表信息(名牌名称，模糊查询)
        System.out.println(shopInfoList);
    }

    /**
     * 获取token
     * @param appKey 应用私钥
     * @param appSecret 应用密钥
     * @param clientId 客户端编号
     * @return
     */
    public static String getAccessToken(String appKey,String appSecret,String clientId) {
        Map<String,String> params = new HashMap<>(3);
        params.put("appKey",appKey);
        params.put("appSecret",appSecret);
        params.put("clientId",clientId);
        String result = HttpUtil.doJsonPost(TOKEN_URI, params,new HashMap<>(0));
        return JacksonUtil.readTree(result).get("data").get("token").asText();
    }

    /**
     * 获取商品列表（品牌名模，糊搜索）
     * @param accessToken 令牌
     * @param page 页码
     * @param size 页数
     * @return
     */
    public static String getShopInfoList(String accessToken,int page,int size,String keyword) {
        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("accessToken",accessToken);
        Map<String,String> params = new HashMap<>(3);
        params.put("size","" + size);
        params.put("page","" + page);
        params.put("brandName",keyword);
        String result = HttpUtil.doJsonPost(SHOP_INFO_URI,params,headers);
        return result;
    }

}
