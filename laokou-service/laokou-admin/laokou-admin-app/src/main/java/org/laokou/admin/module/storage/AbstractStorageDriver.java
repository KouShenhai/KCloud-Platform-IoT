/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.admin.module.storage;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;

import java.io.InputStream;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
public abstract class AbstractStorageDriver<O> implements StorageDriver<O> {

	protected OssCO co;

	public String upload(int limitRead, long fileSize, String fileName, InputStream inputStream, String contentType) {
		try {
			// 修改文件名
			String newFileName = getFileName(fileName);
			// 获取连接对象
			O obj = getObj();
			// 创建bucket
			createBucket(obj);
			// 上传文件
			putObject(obj, limitRead, fileSize, newFileName, inputStream, contentType);
			// 获取地址
			return getUrl(obj, newFileName);
		}
		catch (Exception ex) {
			log.error("文件上传失败，错误信息：{}，详情见日志", LogUtil.result(ex.getMessage()), ex);
			throw new SystemException(String.format("文件上传失败，错误信息：%s", ex.getMessage()));
		}
	}

	/**
	 * 获取连接对象.
	 * @return O
	 */
	protected abstract O getObj();

	/**
	 * 文件后缀.
	 */
	public String getFileExt(String fileName) {
		return FileUtil.getFileExt(fileName);
	}

}
