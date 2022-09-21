/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * http工具类
 * @author  Kou Shenhai
 */
public class HttpContextUtil {

    public static HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            return null;
        }

        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public static Map<String,String> getParameterMap(HttpServletRequest request){
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String,String> params = new HashMap<>();
        while(parameterNames.hasMoreElements()){
            String parameter = parameterNames.nextElement();
            String value = request.getParameter(parameter);
            if (StringUtils.isNotBlank(value)){
                params.put(parameter,value);
            }
        }
        return params;
    }

    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(),url.length()).toString();
    }

    public static String getOrigin(){
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(HttpHeaders.ORIGIN);
    }

    public static String getLanguage(){
        //默认语言
        String defaultLanguage = "z-CN";
        //request
        HttpServletRequest request = getHttpServletRequest();
        if (null == request){
            return defaultLanguage;
        }
        //请求语言
        defaultLanguage = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        return defaultLanguage;
    }

}
