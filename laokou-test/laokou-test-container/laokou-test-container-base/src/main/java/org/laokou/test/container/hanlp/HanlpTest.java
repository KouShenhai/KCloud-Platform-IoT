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
import org.laokou.common.i18n.utils.StringUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Slf4j
public class HanlpTest {

	public static void main(String[] args) {
		// 600 => 长度25
		String str = "laok5";
		// String mail = "@dmin123456789qwerty1@3.55";
		// index(str);
		// standard(str);
		aes(str, 4);
		// aes(mail, 4);
		// aes();
		// log.info("{}", str.length());
		// log.info("<<<<<<<<<<{}", AesUtil.encrypt(str).length());
	}

	private static void aes() {
		Set<String> set = new HashSet<>();
		set.add(AesUtil.encrypt("xxx"));
		set.add(AesUtil.encrypt("xxxx"));
		set.add(AesUtil.encrypt("xxxx"));
		String s = StringUtil.collectionToDelimitedString(set, "~");
		log.info("{}", s);
		log.info(">>>>>>>>>>>{}", s.length());
	}

	private static void aes(String str, int sliceNum) {
		log.info(">>>>>>>>>>>>>   {}", AesUtil.encrypt(str));
		int length = str.length();
		if (length < sliceNum) {
			return;
		}
		Set<String> set = new HashSet<>(length + 1);
		for (int i = 0; i <= length - sliceNum; i++) {
			set.add(AesUtil.encrypt(str.substring(i, i + sliceNum)));
		}
		String s = StringUtil.collectionToDelimitedString(set, "~");
		log.info("{}", s.length());
		log.info("{}", s);
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
