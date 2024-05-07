/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.laokou.admin.domain.oss.File;

/**
 * OSS对外操作接口.
 *
 * @author laokou
 */
public interface StorageDriver<O> {

	/**
	 * 创建bucket.
	 * @param obj 连接对象
	 */
	void createBucket(O obj);

	/**
	 * 上传文件.
	 * @param obj 连接对象
	 * @param file 文件对象
	 */
	void putObject(O obj, File file);

	/**
	 * 获取地址.
	 * @param obj 连接对象
	 * @param file 文件对象
	 * @return 上传成功的链接
	 */
	String getUrl(O obj, File file);

	/**
	 * 上传文件.
	 * @param file 文件对象
	 * @return 上传成功的链接
	 */
	String upload(File file);

	/**
	 * 获取文件名称.
	 * @param fileName 文件名
	 * @return 文件名称
	 */
	String getFileName(String fileName);

	/**
	 * 获取文件后缀.
	 * @param fileName 文件名
	 * @return 文件后缀
	 */
	String getFileExt(String fileName);

}
