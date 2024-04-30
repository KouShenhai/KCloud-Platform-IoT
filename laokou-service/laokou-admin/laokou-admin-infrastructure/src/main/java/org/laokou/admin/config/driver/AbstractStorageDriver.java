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

package org.laokou.admin.config.driver;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.oss.File;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.common.core.utils.FileUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;

/**
 * OSS抽象.
 *
 * @author laokou
 */
@Slf4j
@NonNullApi
public abstract class AbstractStorageDriver<O> implements StorageDriver<O> {

	protected Oss oss;

	/**
	 * 上传文件.
	 * @param file 文件对象
	 * @return 文件对象
	 */
	public String upload(File file) {
		try {
			// 获取连接对象
			O obj = getObj();
			// 创建bucket
			createBucket(obj);
			// 上传文件
			putObject(obj, file);
			// 获取地址
			return getUrl(obj, file);
		}
		catch (Exception ex) {
			String msg = LogUtil.record(ex.getMessage());
			log.error("文件上传失败，错误信息：{}，详情见日志", msg, ex);
			throw new SystemException(String.format("文件上传失败，错误信息：%s", msg));
		}
	}

	/**
	 * 获取连接对象.
	 * @return 连接对象
	 */
	protected abstract O getObj();

	/**
	 * 文件后缀.
	 * @param fileName 文件名称
	 */
	public String getFileExt(String fileName) {
		return FileUtil.getFileExt(fileName);
	}

}
