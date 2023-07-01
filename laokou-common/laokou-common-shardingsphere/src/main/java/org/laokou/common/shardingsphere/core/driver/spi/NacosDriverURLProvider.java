/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.shardingsphere.core.driver.spi;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.shardingsphere.utils.CryptoUtil;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private static final String PREFIX = "ENC(";

	private static final String SUFFIX = ")";

	private static final String PUBLIC_KEY = "public-key";

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
		StringBuilder stringBuilder = new StringBuilder();
		list.forEach(item -> {
			if (item.contains(PREFIX) && item.contains(SUFFIX)) {
//				String[] val = item.split(Constant.RISK);
//				stringBuilder.append(val[0]).append(Constant.RISK).append(" ").append(AesUtil.decrypt(val[1].trim()))
//						.append("\n");
			}
			else {
				stringBuilder.append(item).append("\n");
			}
		});
		return stringBuilder.toString();
	}

	private List<String> getList(String value) {
		List<String> list = new ArrayList<>(50);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
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
				} catch (Exception e) {
					log.error("ShardingSphere decrypt error ", e);
				}
			}
		}
		return cipherText;
	}

}
