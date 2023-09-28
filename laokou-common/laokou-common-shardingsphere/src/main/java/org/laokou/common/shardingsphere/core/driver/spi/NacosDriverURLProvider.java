/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.shardingsphere.utils.CryptoUtil;
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

import static org.laokou.common.i18n.common.Constant.RISK;
import static org.laokou.common.shardingsphere.utils.CryptoUtil.*;

/**
 * @author laokou
 */
@Slf4j
public class NacosDriverURLProvider implements ShardingSphereDriverURLProvider {

	private static final String SHARDING_SPHERE_JDBC = "jdbc:shardingsphere:";

	private static final String NACOS_TYPE = "nacos:";

	private static final String LOCATION = "bootstrap.yml";

	private static final String FORMAT = "yaml";

	private static final Pattern ENC_PATTERN = Pattern.compile("^ENC\\((.*)\\)$");

	@Override
	public boolean accept(String url) {
		return StringUtil.isNotEmpty(url) && url.contains(NACOS_TYPE);
	}

	@SneakyThrows
	@Override
	public byte[] getContent(String url) {
		NacosConfigProperties properties = PropertyUtil.getProperties(NacosConfigProperties.PREFIX,
				NacosConfigProperties.class, LOCATION, FORMAT);
		String group = properties.getGroup();
		NacosConfigManager nacosConfigManager = new NacosConfigManager(properties);
		ConfigService configService = nacosConfigManager.getConfigService();
		String configPath = url.substring(SHARDING_SPHERE_JDBC.length());
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
		List<String> strList = list.stream().filter(i -> i.startsWith(PUBLIC_KEY)).toList();
		String publicKey = "";
		if (CollectionUtil.isNotEmpty(strList)) {
			publicKey = strList.get(0).substring(11).trim();
		}
		StringBuilder stringBuilder = new StringBuilder();
		String finalPublicKey = publicKey;
		list.forEach(item -> {
			if (!item.startsWith(PUBLIC_KEY)) {
				if (item.contains(PREFIX) && item.contains(SUFFIX)) {
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
			while ((str = reader.readLine()) != null) {
				list.add(str);
			}
		}
		catch (IOException e) {
			log.error("错误信息：{}", e.getMessage());
		}
		return list;
	}

	/**
	 * 字符串解密
	 */
	private String decrypt(String publicKey, String cipherText) {
		if (StringUtils.hasText(cipherText)) {
			Matcher matcher = ENC_PATTERN.matcher(cipherText);
			if (matcher.find()) {
				try {
					return CryptoUtil.decrypt(publicKey, matcher.group(1));
				}
				catch (Exception e) {
					log.error("ShardingSphere decrypt error ", e);
				}
			}
		}
		return cipherText;
	}

}
