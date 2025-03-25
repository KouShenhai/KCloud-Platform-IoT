/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
 *
 */

package org.laokou.common.xss.util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.laokou.common.i18n.common.exception.ParamException;
import org.laokou.common.i18n.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
public class XssUtils extends HtmlUtils {

	private static final Pattern[] SCRIPT_PATTERNS = {
			Pattern.compile("<script(.*?)></script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
			Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

	public static String clearHtml(String str) {
		if (StringUtils.isNotEmpty(str)) {
			for (Pattern pattern : SCRIPT_PATTERNS) {
				str = pattern.matcher(str).replaceAll(EMPTY);
			}
			str = clearHtml(str, getSafelist());
		}
		return str;
	}

	public static String clearSql(String str) {
		if (checkSql(str)) {
			throw new ParamException("P_Request_SqlInjection", "参数异常，存在SQL注入风险");
		}
		return str;
	}

	private static String clearHtml(String str, Safelist safelist) {
		return Pattern.compile("\\n").matcher(Jsoup.clean(str, safelist)).replaceAll(EMPTY);
	}

	private static Safelist getSafelist() {
		return Safelist.relaxed()
			.addAttributes("img", "align", "alt", "height", "src", "title", "width")
			.addAttributes("a", "href")
			.addAttributes("blockquote", "cite")
			.addAttributes("q", "cite")
			.addProtocols("a", "href", "ftp", "http", "https", "mailto")
			.addProtocols("blockquote", "cite", "http", "https")
			.addProtocols("cite", "cite", "http", "https")
			.addProtocols("img", "src", "http", "https")
			.addTags("b", "em", "i", "strong", "u")
			.addTags("img")
			.addTags("a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre",
					"q", "small", "span", "strike", "strong", "sub", "sup", "u", "ul");
	}

	private static boolean checkSql(String str) {
		try {
			CCJSqlParserUtil.parse(str);
			return true;
		}
		catch (JSQLParserException e) {
			return false;
		}
	}

}
