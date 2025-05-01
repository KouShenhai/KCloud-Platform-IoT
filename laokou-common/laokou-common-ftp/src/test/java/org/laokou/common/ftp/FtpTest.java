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

package org.laokou.common.ftp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.ftp.config.FtpProperties;
import org.laokou.common.ftp.template.FtpTemplate;
import org.laokou.common.i18n.util.ResourceUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@EnableConfigurationProperties
@ContextConfiguration(classes = { FtpProperties.class, FtpTemplate.class, FtpConfig.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class FtpTest {

	private final FtpTemplate ftpTemplate;

	private final FtpProperties ftpProperties;

	@Test
	void test() throws IOException {
		Assertions.assertDoesNotThrow(() -> ftpTemplate.upload(ftpProperties.getDirectory(), "test.txt",
				ResourceUtils.getResource("classpath:test.txt").getInputStream()));
		InputStream inputStream = ftpTemplate.download(ftpProperties.getDirectory(), "test.txt");
		Assertions.assertNotNull(inputStream);
		Assertions.assertEquals("123", new String(inputStream.readAllBytes()).trim());
		Assertions.assertDoesNotThrow(() -> ftpTemplate.delete(ftpProperties.getDirectory(), "test.txt"));
		Assertions.assertNull(ftpTemplate.download(ftpProperties.getDirectory(), "test.txt"));
	}

}
