/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.laokou.common.i18n.utils.StringUtil;
import java.util.regex.Pattern;

/**
 * @author laokou
 */
public class XssUtil {

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

	public static String clear(String str) {
		if (StringUtil.isNotEmpty(str)) {
			for (Pattern pattern : SCRIPT_PATTERNS) {
				str = pattern.matcher(str).replaceAll("");
			}
			str = clear(str, getSafelist());
		}
		return str;
	}

	private static String clear(String str, Safelist safelist) {
		return Jsoup.clean(str, safelist);
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
					"q", "small", "span", "strike", "strong", "sub", "sup", "u", "ul")
			.addEnforcedAttribute("a", "rel", "nofollow");
	}

}
