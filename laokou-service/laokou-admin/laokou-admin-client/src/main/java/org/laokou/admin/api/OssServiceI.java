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

package org.laokou.admin.api;

import org.laokou.admin.dto.oss.*;
import org.laokou.admin.dto.oss.clientobject.FileCO;
import org.laokou.admin.dto.oss.clientobject.OssCO;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

/**
 * OSS管理.
 *
 * @author laokou
 */
public interface OssServiceI {

	/**
	 * 新增OSS.
	 * @param cmd 新增OSS参数
	 */
	void create(OssCreateCmd cmd);

	/**
	 * 修改OSS.
	 * @param cmd 修改OSS参数
	 */
	void modify(OssModifyCmd cmd);

	/**
	 * 根据IDS删除OSS.
	 * @param cmd 根据IDS删除OSS参数
	 */
	void remove(OssRemoveCmd cmd);

	/**
	 * 根据ID查看OSS.
	 * @param qry 根据ID查看OSS参数
	 * @return OSS
	 */
	Result<OssCO> findById(OssGetQry qry);

	/**
	 * 查询OSS列表.
	 * @param qry 查询OSS列表参数
	 * @return OSS列表
	 */
	Result<Datas<OssCO>> findList(OssListQry qry);

	/**
	 * 上传文件.
	 * @param cmd 上传文件参数
	 * @return 文件对象
	 */
	Result<FileCO> upload(OssUploadCmd cmd);

}
