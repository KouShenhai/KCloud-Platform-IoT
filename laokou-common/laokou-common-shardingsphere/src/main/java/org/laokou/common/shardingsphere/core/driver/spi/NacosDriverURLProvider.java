/**
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
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.i18n.utils.StringUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public class NacosDriverURLProvider implements ShardingSphereDriverURLProvider {

	private static final String NACOS_TYPE = "nacos:";

	private static final String LOCATION = "bootstrap.yml";

	private static final String FORMAT = "yaml";

	public boolean accept(String url) {
		return StringUtil.isNotEmpty(url) && url.contains(NACOS_TYPE);
	}

	@SneakyThrows
	public byte[] getContent(String url) {
		NacosConfigProperties properties = PropertyUtil.getProperties(NacosConfigProperties.PREFIX,
				NacosConfigProperties.class, LOCATION, FORMAT);
		String group = properties.getGroup();
		NacosConfigManager nacosConfigManager = new NacosConfigManager(properties);
		ConfigService configService = nacosConfigManager.getConfigService();
		String configPath = url.substring("jdbc:shardingsphere:".length());
		String dataId = configPath.substring("nacos:".length());
		Preconditions.checkArgument(!dataId.isEmpty(), "Nacos dataId is required in ShardingSphere driver URL.");
		return configService.getConfig(dataId, group, 5000).getBytes(StandardCharsets.UTF_8);
	}

}
