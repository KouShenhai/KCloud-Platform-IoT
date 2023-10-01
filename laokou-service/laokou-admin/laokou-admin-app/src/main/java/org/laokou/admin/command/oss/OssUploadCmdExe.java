/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.command.oss;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.OssLog;
import org.laokou.admin.dto.oss.OssUploadCmd;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.gatewayimpl.database.OssLogMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.admin.module.storage.factory.StorageFactory;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.laokou.admin.common.Constant.MAX_FILE_SIZE;
import static org.laokou.admin.common.Constant.TENANT;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OssUploadCmdExe {

	private final StorageFactory storageFactory;

	private final OssLogMapper ossLogMapper;

	private final OssGateway ossGateway;

	public Result<FileCO> execute(OssUploadCmd cmd) {
		return Result.of(upload(cmd.getFile()));
	}

	@SneakyThrows
	@DS(TENANT)
	private FileCO upload(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		long fileSize = file.getSize();
		// 一次读取字节数
		int limitRead = (int) (fileSize + 1);
		String contentType = file.getContentType();
		InputStream inputStream = file.getInputStream();
		before(fileSize);
		ByteArrayOutputStream bos = getCacheStream(inputStream);
		String md5 = DigestUtils.md5DigestAsHex(new ByteArrayInputStream(bos.toByteArray()));
		OssLogDO ossLogDO = ossLogMapper.selectOne(Wrappers.query(OssLogDO.class).eq("md5", md5).select("url"));
		if (ossLogDO != null) {
			return new FileCO(ossLogDO.getUrl(), md5);
		}
		String url = storageFactory.build(UserUtil.getTenantId())
			.upload(limitRead, fileSize, fileName, new ByteArrayInputStream(bos.toByteArray()), contentType);
		after(new OssLog(md5, url, fileName, fileSize));
		return new FileCO(url, md5);
	}

	@SneakyThrows
	private ByteArrayOutputStream getCacheStream(InputStream inputStream) {
		// 缓存流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.writeBytes(inputStream.readAllBytes());
		return bos;
	}

	private void before(long fileSize) {
		log.info("文件上传前，校验文件大小");
		if (fileSize > MAX_FILE_SIZE) {
			throw new GlobalException("单个文件上传不能超过100M，请重新选择文件并上传");
		}
	}

	private void after(OssLog ossLog) {
		log.info("文件上传后，存入日志");
		ossGateway.publish(ossLog);
	}

}
