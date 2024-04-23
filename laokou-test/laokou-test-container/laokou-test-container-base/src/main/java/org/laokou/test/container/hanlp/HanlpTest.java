/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.test.container.hanlp;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.crypto.utils.AesUtil;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class HanlpTest {

	public static void main(String[] args) {
		String str = "admin211";
		index(str);
		standard(str);
		aes();
	}

	private static void aes() {
		log.info("{}", AesUtil.encrypt("a"));
		log.info("{}", AesUtil.encrypt("ad"));
		log.info("{}", AesUtil.encrypt("adm"));
		log.info("{}", AesUtil.encrypt("admi"));
		log.info("{}", AesUtil.encrypt("admin"));
		log.info("{}", AesUtil.encrypt("admin1"));
		log.info("{}", AesUtil.encrypt("admin12"));
		log.info("{}", AesUtil.encrypt("admin123"));
		log.info("{}", AesUtil.encrypt("admin1234"));
		log.info("{}", AesUtil.encrypt("admin12345"));
		log.info("{}", AesUtil.encrypt("admin123456"));
		log.info("{}", AesUtil.encrypt("admin1234567"));
		log.info("{}", AesUtil.encrypt("admin12345678"));
		log.info("{}", AesUtil.encrypt("admin123456789"));
		log.info("{}", AesUtil.encrypt("ZZZZZ9999999999"));
		log.info("{}", "j1jNpxMAqyu0mO5vtG6mnQ==".length());
		log.info("{}", 24 * 20 + 23);
		log.info("{}", AesUtil.decrypt("NzCOSafMP3ezJtTgyMasGg=="));
	}

	private static void standard(String str) {
		List<Term> termList = StandardTokenizer.segment(str);
		for (Term term : termList) {
			log.info("nlp -> {}", term.word);
		}
		log.info("nlp size -> {}", termList.size());
	}

	private static void index(String str) {
		List<Term> termList = IndexTokenizer.segment(str);
		for (Term term : termList) {
			log.info("index -> {}", term.word);
		}
		log.info("index size -> {}", termList.size());
	}

}
