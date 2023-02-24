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

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.regex.Pattern;

/**
 * http://www.xwood.net/_site_domain_/_root/5870/5874/t_c280386.html
 * XSS过滤工具
 * @author laokou
 */
public class XssUtil {

    private static final Pattern[] SCRIPT_PATTERNS = {
            Pattern.compile("<script(.*?)></script>",Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>",Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:",Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:",Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    /**
     * XSS过滤
     */
    public static String filter(String html){
        return Jsoup.clean(html, Safelist.relaxed());
    }

    public static String clean(String value) {
        if (StringUtil.isNotEmpty(value)) {
            value = value.replaceAll("\0|\n|\r","");
            for (Pattern pattern : SCRIPT_PATTERNS) {
                value = pattern.matcher(value).replaceAll("");
            }
            value = filter(value.replaceAll("'","\""));
        }
        return value;
    }

    public static void main(String[] args) {
        String cleanValue = XssUtil.clean("select & from sql <script>qqqqqq</script>");
        System.out.println(cleanValue);
    }

}
