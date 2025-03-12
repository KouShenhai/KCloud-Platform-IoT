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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.shardingsphere.config;

import com.alibaba.nacos.api.exception.NacosException;
import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.url.core.ShardingSphereURL;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.constant.StringConstant;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.laokou.common.i18n.common.constant.StringConstant.WELL_NO;

/**
 * Abstract ShardingSphere URL load engine.
 *
 * @author laokou
 */
@Slf4j
public abstract class AbstractShardingSphereURLLoadEngine {

	private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

	private static final String PUBLIC_KEY = "public-key";

	private static final String CRYPTO_PREFIX = "ENC(";

	private static final String CRYPTO_SUFFIX = ")";

	protected final ShardingSphereURL url;

	public AbstractShardingSphereURLLoadEngine(final ShardingSphereURL url) {
		this.url = url;
	}

	/**
	 * Load configuration content.
	 * @return loaded content
	 */
	public byte[] loadContent() throws Exception {
		return resolvePropertyValue(getContent()).getBytes(StandardCharsets.UTF_8);
	}

	abstract protected String getContent() throws IOException, NacosException;

	private String resolvePropertyValue(String value) {
		List<String> list = getList(value);
		if (list.isEmpty()) {
			throw new SystemException("S_ShardingSphere_PullConfigError", "无法拉取ShardingSphere配置");
		}
		List<String> strList = list.stream().filter(i -> i.startsWith(PUBLIC_KEY)).toList();
		String publicKey = StringConstant.EMPTY;
		if (CollectionUtil.isNotEmpty(strList)) {
			publicKey = strList.getFirst().substring(11).trim();
		}
		StringBuilder stringBuilder = new StringBuilder();
		String finalPublicKey = publicKey;
		list.forEach(item -> {
			if (!item.startsWith(PUBLIC_KEY)) {
				if (item.contains(CRYPTO_PREFIX) && item.contains(CRYPTO_SUFFIX)) {
					int index = item.indexOf(StringConstant.RISK);
					String key = item.substring(0, index + 2);
					String val = item.substring(index + 2).trim();
					stringBuilder.append(key).append(decrypt(finalPublicKey, val)).append("\n");
				}
				else {
					stringBuilder.append(item).append("\n");
				}
			}
		});
		return stringBuilder.toString();
	}

	private List<String> getList(String value) {
		List<String> list = new ArrayList<>(50);
		try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(
				new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8))) {
			String str;
			while (ObjectUtil.isNotNull((str = reader.readLine()))) {
				// #开头直接丢弃
				if (!str.trim().startsWith(WELL_NO)) {
					list.add(str);
				}
			}
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
		}
		return list;
	}

	/**
	 * 字符串解密.
	 * @param cipherText 加密字符串
	 * @param publicKey 公钥
	 */
	private String decrypt(String publicKey, String cipherText) {
		if (StringUtils.hasText(cipherText)) {
			Matcher matcher = ENC_PATTERN.matcher(cipherText);
			if (matcher.find()) {
				try {
					return CryptoUtils.decrypt(publicKey, matcher.group(1));
				}
				catch (Exception e) {
					log.error("ShardingSphere decrypt error", e);
				}
			}
		}
		return cipherText;
	}

}
