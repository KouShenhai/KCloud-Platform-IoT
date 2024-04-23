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

package org.laokou.common.shardingsphere.core.driver.spi;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.google.common.base.Preconditions;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereURLProvider;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.crypto.utils.RsaUtil;
import org.laokou.common.i18n.utils.LogUtil;
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

import static com.alibaba.cloud.nacos.NacosDiscoveryProperties.PREFIX;
import static org.laokou.common.i18n.common.ShardingSphereConstant.*;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.StringConstant.RISK;

/**
 * @author laokou
 */
@Slf4j
public class NacosDriverURLProvider implements ShardingSphereURLProvider {

	@Schema(name = "CRYPTO_PREFIX", description = "加密前缀")
	private static final String CRYPTO_PREFIX = "ENC(";

	@Schema(name = "CRYPTO_SUFFIX", description = "加密后缀")
	private static final String CRYPTO_SUFFIX = ")";

	@Schema(name = "PRIVATE_KEY", description = "私钥Key")
	private static final String PRIVATE_KEY = "private-key";

	@Override
	public boolean accept(String url) {
		return StringUtil.isNotEmpty(url) && url.contains(NACOS_TYPE);
	}

	@SneakyThrows
	@Override
	public byte[] getContent(String url, String prefix) {
		NacosConfigProperties properties = PropertyUtil.getProperties(PREFIX, NacosConfigProperties.class,
				YAML_LOCATION, YAML_FORMAT);
		String group = properties.getGroup();
		NacosConfigManager nacosConfigManager = new NacosConfigManager(properties);
		ConfigService configService = nacosConfigManager.getConfigService();
		String configPath = url.substring(JDBC_TYPE.length());
		String dataId = configPath.substring(NACOS_TYPE.length());
		Preconditions.checkArgument(!dataId.isEmpty(), "Nacos dataId is required in ShardingSphere driver URL.");
		String configInfo = configService.getConfig(dataId, group, 5000);
		return resolvePropertyValue(configInfo).getBytes(StandardCharsets.UTF_8);
	}

	private String resolvePropertyValue(String value) {
		List<String> list = getList(value);
		if (list.isEmpty()) {
			throw new RuntimeException("Nacos配置ShardingSphere不正确");
		}
		List<String> strList = list.stream().filter(i -> i.startsWith(PRIVATE_KEY)).toList();
		String privateKey = EMPTY;
		if (CollectionUtil.isNotEmpty(strList)) {
			privateKey = strList.getFirst().substring(12).trim();
		}
		StringBuilder stringBuilder = new StringBuilder();
		String pk = privateKey;
		list.forEach(item -> {
			if (!item.startsWith(PRIVATE_KEY)) {
				if (item.contains(CRYPTO_PREFIX) && item.contains(CRYPTO_SUFFIX)) {
					int index = item.indexOf(RISK);
					String key = item.substring(0, index + 2);
					String val = item.substring(index + 2).trim();
					stringBuilder.append(key).append(decrypt(pk, val)).append("\n");
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
			log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
		}
		return list;
	}

	/**
	 * 字符串解密.
	 * @param cipherText 加密字符串
	 * @param privateKey 私钥
	 */
	private String decrypt(String privateKey, String cipherText) {
		if (StringUtils.hasText(cipherText)) {
			Matcher matcher = ENC_PATTERN.matcher(cipherText);
			if (matcher.find()) {
				try {
					return RsaUtil.decryptByPrivateKey(matcher.group(1), privateKey);
				}
				catch (Exception e) {
					log.error("ShardingSphere decrypt error", e);
				}
			}
		}
		return cipherText;
	}

}
