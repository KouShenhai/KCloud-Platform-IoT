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

package org.laokou.common.ftp.template;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.ftp.config.SFtpProperties;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author laokou
 */
@Slf4j
@Component
public class SFtpTemplate {

	private Session session;

	private ChannelSftp channelSftp;

	public SFtpTemplate(SFtpProperties sftpProperties) {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
			session.setPassword(sftpProperties.getPassword());
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "password,keyboard-interactive");
			session.setConfig(config);
			session.connect();
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
		}
		catch (Exception e) {
			log.error("【SFTP】 => 执行失败，错误信息：{}", e.getMessage(), e);
		}
	}

	public boolean upload(String directory, String fileName, InputStream in) {
		try {
			try {
				channelSftp.cd(directory);
			}
			catch (Exception e) {
				log.error("【SFTP】 => 进入目录失败，错误信息：{}", e.getMessage(), e);
				channelSftp.mkdir(directory);
				channelSftp.cd(directory);
			}
			channelSftp.put(in, fileName);
			return true;
		}
		catch (Exception e) {
			log.error("【SFTP】 => 上传文件失败，错误信息：{}", e.getMessage(), e);
			return false;
		}
	}

	public InputStream download(String directory, String fileName) {
		try {
			channelSftp.cd(directory);
			return channelSftp.get(fileName);
		}
		catch (Exception e) {
			log.error("【SFTP】 => 下载文件失败，错误信息：{}", e.getMessage(), e);
			return null;
		}
	}

	public boolean delete(String directory, String fileName) {
		try {
			channelSftp.cd(directory);
			channelSftp.rm(fileName);
			return true;
		}
		catch (Exception e) {
			log.error("【SFTP】 => 删除文件失败，错误信息：{}", e.getMessage(), e);
			return false;
		}
	}

	@PreDestroy
	public void close() {
		if (ObjectUtils.isNotNull(channelSftp) && channelSftp.isConnected()) {
			channelSftp.disconnect();
		}
		if (ObjectUtils.isNotNull(session) && session.isConnected()) {
			session.disconnect();
		}
	}

}
