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
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FtpTemplate {

	private final FTPClient ftpClient;

	public void upload(String directory, String fileName, InputStream in) {
		try {
			if (!ftpClient.changeWorkingDirectory(directory)) {
				ftpClient.makeDirectory(directory);
				ftpClient.changeWorkingDirectory(directory);
			}
			ftpClient.storeFile(fileName, in);
		}
		catch (Exception e) {
			log.error("【FTP】 => 上传文件失败，错误信息：{}", e.getMessage(), e);
		}
	}

	public InputStream download(String directory, String fileName) {
		try {
			ftpClient.changeWorkingDirectory(directory);
			return ftpClient.retrieveFileStream(fileName);
		}
		catch (Exception e) {
			log.error("【FTP】 => 下载文件失败，错误信息：{}", e.getMessage(), e);
			return null;
		}
	}

	public void delete(String directory, String fileName) {
		try {
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.deleteFile(fileName);
		}
		catch (Exception e) {
			log.error("【FTP】 => 删除文件失败，错误信息：{}", e.getMessage(), e);
		}
	}

}
