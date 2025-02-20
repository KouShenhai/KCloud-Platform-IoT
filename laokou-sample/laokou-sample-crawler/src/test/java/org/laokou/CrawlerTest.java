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

package org.laokou;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.i18n.utils.SslUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
class CrawlerTest {

	private static final Map<String, String> MAP = new LinkedHashMap<>();

	private static final String DIRECTORY = "D:/crawl/data/";

	static {
		MAP.put("xxx", "xxx");
	}

	@Test
	void testCrawl() {
		MAP.forEach((name, url) -> {
			try {
				crawl(url, name);
			}
			catch (IOException | InterruptedException | NoSuchAlgorithmException | KeyManagementException e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void crawl(String url, String name)
			throws IOException, InterruptedException, NoSuchAlgorithmException, KeyManagementException {
		String directory = DIRECTORY + name;
		Document document = Jsoup.connect(url).get();
		Elements links = document.select("a[href]");
		int index = 0;
		for (Element link : links) {
			// 提取链接的文本和URL
			String linkText = link.text();
			String linkHref = link.attr("abs:href");
			if (linkHref.startsWith("https://xxx/%")) {
				SslUtil.ignoreSSLTrust();
				try {
					FileUtil.write(FileUtil.create(directory, linkText.replace(".md", ".html")),
							getContent(directory, index, linkHref).getBytes(StandardCharsets.UTF_8));
				}
				catch (Exception e) {
					continue;
				}
				// 每10秒钟抓取一次
				Thread.sleep(10000);
				index++;
			}
		}
	}

	private String getImgDirectory(String directory, int index) {
		return directory + "/" + index;
	}

	private String getContent(String directory, int index, String url) throws IOException {
		return getHeadContent() + "\n" + getBodyContent(directory, index, url);
	}

	private String getBodyContent(String directory, int index, String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		// 使用选择器选择所有具有 class="book-container" 的 <div> 元素
		Element element = document.select("div.book-container").getFirst();
		element.select("div.book-sidebar").remove();
		setImg(directory, index, element);
		return element.html();
	}

	private void setImg(String directory, int index, Element element) throws IOException {
		Elements imgElement = element.select("img[src]");
		int offset = 0;
		for (Element e : imgElement) {
			String src = e.attr("abs:src");
			Path path = FileUtil.create(getImgDirectory(directory, index), offset + ".png");
			FileUtil.write(path, FileUtil.getBytes(src));
			e.attr("style", "width: 100%;");
			e.attr("src", getImgPath(path, index));
			offset++;
		}
	}

	private String getImgPath(Path path, int index) {
		return index + "/" + path.getFileName();
	}

	private String getHeadContent() {
		return "<link rel='stylesheet' href='css/index.css'>" + "<link rel='stylesheet' href='css/highlight.min.css'>"
				+ "<script src='js/highlight.min.js'></script>"
				+ "<script defer src='js/script.js' data-website-id='83e5d5db-9d06-40e3-b780-cbae722fdf8c'></script>";
	}

}
