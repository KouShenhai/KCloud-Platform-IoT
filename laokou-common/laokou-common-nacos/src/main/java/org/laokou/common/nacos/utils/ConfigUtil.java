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

package org.laokou.common.nacos.utils;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public final class ConfigUtil {

	private final NacosConfigManager nacosConfigManager;

	/**
	 * 获取分组名称.
	 */
	public String getGroup() {
		return nacosConfigManager.getNacosConfigProperties().getGroup();
	}

	/**
	 * 创建配置服务.
	 * @param serverAddr 服务地址
	 */
	public static ConfigService createConfigService(String serverAddr) throws NacosException {
		return NacosFactory.createConfigService(serverAddr);
	}

	/**
	 * 创建服务地址.
	 * @param properties 配置
	 */
	public static ConfigService createConfigService(Properties properties) throws NacosException {
		return NacosFactory.createConfigService(properties);
	}

	// @formatter:off
	/**
	 * 获取配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param timeoutMs 读取配置超时时间，单位 ms，推荐值 3000
	 * @return String
	 */
	public String getConfig(String dataId, String group, long timeoutMs) throws NacosException {
		return getConfigService().getConfig(dataId, group, timeoutMs);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 获取配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param timeoutMs 读取配置超时时间，单位 ms，推荐值 3000
	 * @param listener 监听器，配置变更进入监听器的回调函数
	 * @return String
	 */
	public String getConfig(String dataId, String group, long timeoutMs, Listener listener) throws NacosException {
		return getConfigService().getConfigAndSignListener(dataId, group, timeoutMs, listener);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 增加监听配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param listener 监听器，配置变更进入监听器的回调函数
	 */
	public void addListener(String dataId, String group, Listener listener) throws NacosException {
		getConfigService().addListener(dataId, group, listener);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 删除监听配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param listener 监听器，配置变更进入监听器的回调函数
	 */
	public void removeListener(String dataId, String group, Listener listener) {
		getConfigService().removeListener(dataId, group, listener);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 发布配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param content 配置内容
	 */
	public boolean publishConfig(String dataId, String group, String content) throws NacosException {
		return getConfigService().publishConfig(dataId, group, content);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 发布配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param content 配置内容
	 * @param type 配置类型【properties/xml/json/text/html/yaml/toml/unset】，默认为text
	 */
	public boolean publishConfig(String dataId, String group, String content, String type) throws NacosException {
		return getConfigService().publishConfig(dataId, group, content, type);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 发布配置【CAS】.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param content 配置内容
	 * @param casMd5 配置内容md5
	 */
	public boolean publishConfigCas(String dataId, String group, String content, String casMd5) throws NacosException {
		return getConfigService().publishConfigCas(dataId, group, content, casMd5);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 发布配置【CAS】.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 * @param content 配置内容
	 * @param casMd5 配置内容md5
	 * @param type 配置类型【properties/xml/json/text/html/yaml/toml/unset】，默认为text
	 */
	public boolean publishConfigCas(String dataId, String group, String content, String casMd5, String type) throws NacosException {
		return getConfigService().publishConfigCas(dataId, group, content, casMd5, type);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 移除配置.
	 * @param dataId 配置 ID，采用类似 package.class（如com.taobao.tc.refund.log.level）的命名规则保证全局唯一性，class 部分建议是配置的业务含义。全部字符小写。只允许英文字符和 4 种特殊字符（”.”、”:”、”-”、”_”），不超过 256 字节
	 * @param group 配置分组，建议填写产品名:模块名（Nacos）保证唯一性，只允许英文字符和4种特殊字符（”.”、”:”、”-”、”_”），不超过 128 字节
	 */
	public boolean removeConfig(String dataId, String group) throws NacosException {
		return getConfigService().removeConfig(dataId, group);
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 获取服务状态.
	 */
	public String getServerStatus() {
		return getConfigService().getServerStatus();
	}
	// @formatter:on

	// @formatter:off
	/**
	 * 获取配置服务.
	 */
	private ConfigService getConfigService() {
		return nacosConfigManager.getConfigService();
	}
	// @formatter:on

}
