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

package org.laokou;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.i18n.utils.SslUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
class CrawlerTest {

	private static final String DIRECTORY = "D:/crawl/data/xxx";

	@Test
	@SneakyThrows
	void test_learn_lianglianglee_crawl() {
		String url = "xxx";
		Document document = Jsoup.connect(url).get();
		Elements links = document.select("a[href]");
		int index = 0;
		for (Element link : links) {
			// 提取链接的文本和URL
			String linkText = link.text();
			String linkHref = link.attr("abs:href");
			if (linkHref.startsWith("https://learn.lianglianglee.com/%")) {
				SslUtil.ignoreSSLTrust();
				FileUtil.write(FileUtil.create(DIRECTORY, linkText.replace(".md", ".html")),
						getContent(index, linkHref).getBytes(StandardCharsets.UTF_8));
				// 每10秒钟抓取一次
				Thread.sleep(10000);
				index++;
			}
		}
	}

	private String getImgDirectory(int index) {
		return DIRECTORY + "/" + index;
	}

	private String getContent(int index, String url) {
		return getHeadContent() + "\n" + getBodyContent(index, url);
	}

	@SneakyThrows
	private String getBodyContent(int index, String url) {
		Document document = Jsoup.connect(url).get();
		// 使用选择器选择所有具有 class="book-post" 的 <div> 元素
		Element element = document.select("div.book-container").getFirst();
		element.select("div.book-sidebar").remove();
		setImg(index, element);
		return element.html();
	}

	private void setImg(int index, Element element) {
		Elements imgElement = element.select("img[src]");
		int offset = 0;
		for (Element e : imgElement) {
			String src = e.attr("abs:src");
			File file = FileUtil.create(getImgDirectory(index), offset + ".png");
			FileUtil.write(file, FileUtil.getBytes(src));
			e.attr("src", file.getAbsolutePath());
			offset++;
		}
	}

	private String getHeadContent() {
		return """
				<link rel='stylesheet' href='css/index.css'>
				<link rel='stylesheet' href='css/highlight.min.css'>
				<script src='js/highlight.min.js'></script>
				<script defer src='js/script.js' data-website-id='83e5d5db-9d06-40e3-b780-cbae722fdf8c'></script>
				""";
	}

}
