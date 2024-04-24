/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.oss;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.SneakyThrows;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author laokou
 */
@Data
@Schema(name = "File", description = "文件")
public class File extends AggregateRoot<Long> {

	@Schema(name = "MAX_FILE_SIZE", description = "最大上传文件大小")
	private static final long MAX_FILE_SIZE = 100 * 1024 * 1024;

	@Schema(name = "bosCache", description = "字节输出流缓存")
	private ByteArrayOutputStream bosCache;

	@Schema(name = "limitRead", description = "一次性读取字节数")
	private int limitRead;

	@Schema(name = "contentType", description = "内容类型")
	private String contentType;

	@Schema(name = "md5", description = "文件的MD5标识")
	private String md5;

	@Schema(name = "url", description = "文件的URL")
	private String url;

	@Schema(name = "name", description = "文件名称")
	private String name;

	@Schema(name = "size", description = "文件大小")
	private Long size;

	@SneakyThrows
	public File(MultipartFile file) {
		this.bosCache = getCacheStream(file.getInputStream());
		this.size = file.getSize();
		this.limitRead = (int) (this.size + 1);
		this.md5 = DigestUtils.md5DigestAsHex(new ByteArrayInputStream(this.bosCache.toByteArray()));
		this.contentType = file.getContentType();
		this.name = file.getOriginalFilename();
	}

	public void modifyUrl(Exception e, String url, String appName) {
		if (ObjectUtil.isNotNull(e)) {
			ossUploadFail(url, e, appName);
		}
		else {
			ossUploadSuccess(url, appName);
		}
		this.url = url;
	}

	private void ossUploadSuccess(String url, String appName) {
		// addEvent(new FileUploadSucceededEvent(convert(this, url, EMPTY),
		// UserContextHolder.get(), appName));
	}

	private void ossUploadFail(String url, Exception e, String appName) {
		// addEvent(new FileUploadFailedEvent(convert(this, url, e.getMessage()),
		// UserContextHolder.get(), appName));
	}

	public void checkSize() {
		if (size > MAX_FILE_SIZE) {
			throw new SystemException("单个文件上传不能超过100M，请重新选择文件并上传");
		}
	}

	@SneakyThrows
	private ByteArrayOutputStream getCacheStream(InputStream inputStream) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(inputStream.readAllBytes());
		return bos;
	}

	// private OssLog convert(File file, String url, String errorMessage) {
	// return OssLog.builder()
	// .md5(file.getMd5())
	// .url(url)
	// .name(file.getName())
	// .size(file.getSize())
	// .errorMessage(errorMessage)
	// .build();
	// }

}
