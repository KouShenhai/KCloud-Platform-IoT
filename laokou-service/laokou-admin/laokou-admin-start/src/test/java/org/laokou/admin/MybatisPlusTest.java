/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.admin;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariCpConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.admin.common.utils.DsUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MybatisPlusTest extends CommonTest {

	private static final String DS_NAME = "test";

	private final DsUtil dsUtil;

	private final MybatisUtil mybatisUtil;

	MybatisPlusTest(WebApplicationContext webApplicationContext, OAuth2AuthorizationService oAuth2AuthorizationService,
			DsUtil dsUtil, MybatisUtil mybatisUtil) {
		super(webApplicationContext, oAuth2AuthorizationService);
		this.dsUtil = dsUtil;
		this.mybatisUtil = mybatisUtil;
	}

	@Test
	void mybatisPlusBatchSaveTest() {
		// 新增数据源
		addDs();
		// 测试一百万数据
		int size = 1000000;
		// List<TestDO> list = new ArrayList<>(size);
		// for (long i = 1; i <= size; i++) {
		// list.add(new TestDO(i, EMPTY + i, 0L));
		// }
		long start = IdGenerator.SystemClock.now();
		// mybatisUtil.batch(list, TestMapper.class, DS_NAME, TestMapper::save);
		long end = IdGenerator.SystemClock.now();
		log.info("批量插入一百万数据，消耗时间：{}毫秒", (end - start));
	}

	private void addDs() {
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUrl(
				"jdbc:mysql://mysql.laokou.org:3306/test?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&rewriteBatchedStatements=true");
		properties.setUsername("root");
		properties.setPassword("laokou123");
		properties.setDriverClassName("com.mysql.cj.jdbc.Driver");
		HikariCpConfig hikariCpConfig = new HikariCpConfig();
		hikariCpConfig.setConnectionTimeout(180000L);
		properties.setHikari(hikariCpConfig);
		dsUtil.addDs(DS_NAME, properties);
	}

}
