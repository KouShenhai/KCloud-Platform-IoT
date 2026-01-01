/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.ftp;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.laokou.common.ftp.config.FtpProperties;
import org.laokou.common.ftp.template.FtpTemplate;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.testcontainers.container.FtpContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestConstructor;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@EnableConfigurationProperties
@ContextConfiguration(classes = { FtpProperties.class, FtpTemplate.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FtpTest {

	private final FtpTemplate ftpTemplate;

	private final FtpProperties ftpProperties;

	static final FtpContainer ftp = new FtpContainer(DockerImageNames.ftp()).withUsername("root")
		.withPassword("laokou123");

	@BeforeAll
	static void beforeAll() {
		ftp.start();
	}

	@AfterAll
	static void afterAll() {
		ftp.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.ftp.host", ftp::getHost);
		registry.add("spring.ftp.port", () -> ftp.getMappedPort(21));
	}

	@Test
	void test_ftp() throws IOException {
		Assertions.assertThat(ftpTemplate.upload(ftpProperties.getDirectory(), "测试中文文本.txt",
				ResourceExtUtils.getResource("测试中文文本.txt").getInputStream()))
			.isTrue();
		InputStream inputStream = ftpTemplate.download(ftpProperties.getDirectory(), "测试中文文本.txt");
		Assertions.assertThat(inputStream).isNotNull();
		Assertions.assertThat(new String(inputStream.readAllBytes()).trim()).isEqualTo("123");
		Assertions.assertThat(ftpTemplate.delete(ftpProperties.getDirectory(), "测试中文文本.txt")).isTrue();
		Assertions.assertThat(ftpTemplate.download(ftpProperties.getDirectory(), "测试中文文本.txt")).isNull();
	}

}
