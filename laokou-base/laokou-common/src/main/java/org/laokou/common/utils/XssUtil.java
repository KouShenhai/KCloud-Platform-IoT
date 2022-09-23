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
package org.laokou.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.regex.Pattern;

/**
 * XSS过滤工具
 * @author  Kou Shenhai
 */
public class XssUtil extends Whitelist {

    private static final Pattern[] scriptPatterns = {
            Pattern.compile("<script(.*?)></script>",Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
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
        return Jsoup.clean(html, xssWhitelist());
    }

    public static String clean(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll("\0|\n|\r","");
            for (Pattern pattern : scriptPatterns) {
                value = pattern.matcher(value).replaceAll("");
            }
            value = filter(value.replaceAll("'","“"));
        }
        return value;
    }

    /**
     * XSS过滤白名单
     */
    private static Whitelist xssWhitelist(){
        return new Whitelist()
                //支持的标签
                .addTags("a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd", "div", "dl",
                        "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", "i", "img", "li", "ol", "p", "pre", "q", "small",
                        "strike", "strong","sub", "sup", "table", "tbody", "td","tfoot", "th", "thead", "tr", "u","ul",
                        "embed","object","param","span")

                //支持的标签属性
                .addAttributes("a", "href", "class", "style", "target", "rel", "nofollow")
                .addAttributes("blockquote", "cite")
                .addAttributes("code", "class", "style")
                .addAttributes("col", "span", "width")
                .addAttributes("colgroup", "span", "width")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width", "class", "style")
                .addAttributes("ol", "start", "type")
                .addAttributes("q", "cite")
                .addAttributes("table", "summary", "width", "class", "style")
                .addAttributes("tr", "abbr", "axis", "colspan", "rowspan", "width", "style")
                .addAttributes("td", "abbr", "axis", "colspan", "rowspan", "width", "style")
                .addAttributes("th", "abbr", "axis", "colspan", "rowspan", "scope","width", "style")
                .addAttributes("ul", "type", "style")
                .addAttributes("pre", "class", "style")
                .addAttributes("div", "class", "id", "style")
                .addAttributes("embed", "src", "wmode", "flashvars", "pluginspage", "allowFullScreen", "allowfullscreen",
                        "quality", "width", "height", "align", "allowScriptAccess", "allowscriptaccess", "allownetworking", "type")
                .addAttributes("object", "type", "id", "name", "data", "width", "height", "style", "classid", "codebase")
                .addAttributes("param", "name", "value")
                .addAttributes("span", "class", "style")

                //标签属性对应的协议
                .addProtocols("a", "href", "ftp", "http", "https", "mailto")
                .addProtocols("img", "src", "http", "https")
                .addProtocols("blockquote", "cite", "http", "https")
                .addProtocols("cite", "cite", "http", "https")
                .addProtocols("q", "cite", "http", "https")
                .addProtocols("embed", "src", "http", "https");
    }

}
