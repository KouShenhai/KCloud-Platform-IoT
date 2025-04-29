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

package org.laokou.common.ftp.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author laokou
 */
@Configuration
public class FtpConfig {

	@Bean(destroyMethod = "disconnect")
	public FTPClient ftpClient(FtpProperties ftpProperties) throws IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
		if (ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword())) {
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		}
		// 被动模式
		ftpClient.enterLocalPassiveMode();
		ftpClient.setControlEncoding("UTF-8");
		return ftpClient;
	}

}
