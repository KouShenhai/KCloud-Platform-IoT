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

package org.laokou.common.shardingsphere.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.url.core.ShardingSphereURL;
import org.laokou.common.core.utils.PropertyUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.shardingsphere.config.AbstractShardingSphereURLLoadEngine;
import java.io.IOException;

/**
 * Nacos ShardingSphere URL load engine.
 *
 * @author laokou
 */
@Slf4j
public final class NacosShardingSphereURLLoadEngine extends AbstractShardingSphereURLLoadEngine {

	private static final String BIND_YAML_NAME = "bootstrap.yml";

	private static final String YAML_FORMAT = "yaml";

	private static final String NACOS_CONFIG_PREFIX = "spring.cloud.nacos.config";

	public NacosShardingSphereURLLoadEngine(ShardingSphereURL url) {
		super(url);
	}

	@Override
	protected String getContent() throws IOException, NacosException {
		// Nacos拉取配置
		NacosConfigProperties properties = PropertyUtil.bindOrCreate(NACOS_CONFIG_PREFIX, NacosConfigProperties.class,
				BIND_YAML_NAME, YAML_FORMAT);
		String group = properties.getGroup();
		NacosConfigManager nacosConfigManager = new NacosConfigManager(properties);
		ConfigService configService = nacosConfigManager.getConfigService();
		String dataId = url.getConfigurationSubject();
		Preconditions.checkArgument(StringUtil.isNotEmpty(dataId),
				"Nacos dataId is required in ShardingSphere driver URL.");
		return configService.getConfig(dataId, group, 5000);
	}

}
