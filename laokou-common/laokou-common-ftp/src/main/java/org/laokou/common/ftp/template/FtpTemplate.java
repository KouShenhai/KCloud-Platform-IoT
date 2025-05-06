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

package org.laokou.common.ftp.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.laokou.common.ftp.config.FtpProperties;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FtpTemplate {

	private final FtpProperties ftpProperties;

	public void upload(String directory, String fileName, InputStream in) throws IOException {
		execute((ftpClient) -> {
			try {
				if (!ftpClient.changeWorkingDirectory(directory)) {
					ftpClient.makeDirectory(directory);
					ftpClient.changeWorkingDirectory(directory);
				}
				ftpClient.storeFile(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1),
						in);
			}
			catch (Exception e) {
				log.error("【FTP】 => 上传文件失败，错误信息：{}", e.getMessage(), e);
			}
		});
	}

	public InputStream download(String directory, String fileName) throws IOException {
		return execute((ftpClient) -> {
			try {
				ftpClient.changeWorkingDirectory(directory);
				return ftpClient.retrieveFileStream(
						new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
			}
			catch (Exception e) {
				log.error("【FTP】 => 下载文件失败，错误信息：{}", e.getMessage(), e);
				return null;
			}
		});
	}

	public void delete(String directory, String fileName) throws IOException {
		execute((ftpClient) -> {
			try {
				ftpClient.changeWorkingDirectory(directory);
				ftpClient
					.deleteFile(new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
			}
			catch (Exception e) {
				log.error("【FTP】 => 删除文件失败，错误信息：{}", e.getMessage(), e);
			}
		});
	}

	private <T> T execute(FtpExecutor<T> executor) throws IOException {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFtpClient();
			return executor.execute(ftpClient);
		}
		catch (Exception e) {
			return null;
		}
		finally {
			close(ftpClient);
		}
	}

	private void execute(FtpExecutorVoid executor) throws IOException {
		FTPClient ftpClient = null;
		try {
			ftpClient = getFtpClient();
			executor.execute(ftpClient);
		}
		finally {
			close(ftpClient);
		}
	}

	private FTPClient getFtpClient() throws IOException {
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

	private void close(FTPClient ftpClient) throws IOException {
		if (ObjectUtils.isNotNull(ftpClient) && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}

	@FunctionalInterface
	public interface FtpExecutor<T> {

		T execute(FTPClient ftpClient);

	}

	@FunctionalInterface
	public interface FtpExecutorVoid {

		void execute(FTPClient ftpClient);

	}

}
