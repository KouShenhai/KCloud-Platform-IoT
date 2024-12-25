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

package org.laokou.infrastructure.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.url.core.ShardingSphereURL;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
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

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.StringConstant.RISK;

/**
 * Nacos ShardingSphere URL load engine.
 *
 * @author laokou
 */
@Slf4j
public final class NacosShardingSphereURLLoadEngine {

	private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

	private static final String BIND_YAML_NAME = "bootstrap.yml";

	private static final String YAML_FORMAT_NAME = "yaml";

	private static final String PUBLIC_KEY_NAME = "public-key";

	private static final String CRYPTO_PREFIX_NAME = "ENC(";

	private static final String CRYPTO_SUFFIX_NAME = ")";

	private static final String NACOS_CONFIG_PREFIX = "spring.cloud.nacos.config";

	private final ShardingSphereURL url;

	public NacosShardingSphereURLLoadEngine(final ShardingSphereURL url) {
		this.url = url;
	}

	/**
	 * Load configuration content.
	 * @return loaded content
	 */
	@SneakyThrows
	public byte[] loadContent() {
		NacosConfigProperties properties = PropertyUtil.bindOrCreate(NACOS_CONFIG_PREFIX, NacosConfigProperties.class,
				BIND_YAML_NAME, YAML_FORMAT_NAME);
		String group = properties.getGroup();
		NacosConfigManager nacosConfigManager = new NacosConfigManager(properties);
		ConfigService configService = nacosConfigManager.getConfigService();
		String dataId = url.getConfigurationSubject();
		Preconditions.checkArgument(StringUtil.isNotEmpty(dataId),
				"Nacos dataId is required in ShardingSphere driver URL.");
		String configInfo = configService.getConfig(dataId, group, 5000);
		return resolvePropertyValue(configInfo).getBytes(StandardCharsets.UTF_8);
	}

	private String resolvePropertyValue(String value) {
		List<String> list = getList(value);
		if (list.isEmpty()) {
			throw new RuntimeException("Nacos配置ShardingSphere不正确");
		}
		List<String> strList = list.stream().filter(i -> i.startsWith(PUBLIC_KEY_NAME)).toList();
		String publicKey = EMPTY;
		if (CollectionUtil.isNotEmpty(strList)) {
			publicKey = strList.getFirst().substring(11).trim();
		}
		StringBuilder stringBuilder = new StringBuilder();
		String finalPublicKey = publicKey;
		list.forEach(item -> {
			if (!item.startsWith(PUBLIC_KEY_NAME)) {
				if (item.contains(CRYPTO_PREFIX_NAME) && item.contains(CRYPTO_SUFFIX_NAME)) {
					int index = item.indexOf(RISK);
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
				list.add(str);
			}
		}
		catch (IOException e) {
			log.error("错误信息：{}，详情见日志", e.getMessage(), e);
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
					log.error("Nacos ShardingSphere decrypt error", e);
				}
			}
		}
		return cipherText;
	}

}
