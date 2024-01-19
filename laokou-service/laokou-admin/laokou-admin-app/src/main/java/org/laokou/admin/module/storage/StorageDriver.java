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

package org.laokou.admin.module.storage;

import java.io.InputStream;

/**
 * OSS对外操作接口.
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
	 * @param readLimit 读取时间
	 * @param fileSize 文件大小
	 * @param fileName 文件名
	 * @param inputStream 输入流
	 * @param contentType 类型
	 */
	void putObject(O obj, int readLimit, long fileSize, String fileName, InputStream inputStream, String contentType);

	/**
	 * 获取地址.
	 * @param obj 连接对象
	 * @param fileName 文件名
	 * @return 上传成功的链接
	 */
	String getUrl(O obj, String fileName);

	/**
	 * 上传文件.
	 * @param fileName 文件名
	 * @param inputStream 输入流
	 * @param contentType 类型
	 * @param fileSize 文件大小
	 * @param readLimit 读取时间
	 * @return 上传成功的链接
	 */
	String upload(int readLimit, long fileSize, String fileName, InputStream inputStream, String contentType);

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
