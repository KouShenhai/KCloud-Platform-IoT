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

package org.laokou.oss.convertor;

import org.laokou.oss.dto.clientobject.OssLogCO;
import org.laokou.oss.dto.domainevent.OssUploadEvent;
import org.laokou.oss.factory.OssDomainFactory;
import org.laokou.oss.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.oss.model.OssLogE;

/**
 * OSS日志转换器.
 *
 * @author laokou
 */
public class OssLogConvertor {

	public static OssLogCO toClientObject(OssUploadEvent evt) {
		OssLogCO ossLogCO = new OssLogCO();
		ossLogCO.setId(evt.getId());
		ossLogCO.setName(evt.getName());
		ossLogCO.setMd5(evt.getMd5());
		ossLogCO.setUrl(evt.getUrl());
		ossLogCO.setSize(evt.getSize());
		ossLogCO.setFormat(evt.getFormat());
		ossLogCO.setContentType(evt.getContentType());
		ossLogCO.setOssId(evt.getOssId());
		ossLogCO.setUserId(evt.getUserId());
		ossLogCO.setUploadTime(evt.getUploadTime());
		ossLogCO.setTenantId(evt.getTenantId());
		ossLogCO.setType(evt.getType());
		return ossLogCO;
	}

	public static OssLogDO toDataObject(OssLogE ossLogE) {
		OssLogDO ossLogDO = new OssLogDO();
		ossLogDO.setName(ossLogE.getName());
		ossLogDO.setMd5(ossLogE.getMd5());
		ossLogDO.setUrl(ossLogE.getUrl());
		ossLogDO.setSize(ossLogE.getSize());
		ossLogDO.setOssId(OssDomainFactory.getOss().getId());
		ossLogDO.setFormat(ossLogE.getFormat());
		ossLogDO.setContentType(ossLogE.getContentType());
		ossLogDO.setOssId(ossLogE.getOssId());
		ossLogDO.setCreator(ossLogE.getUserId());
		ossLogDO.setEditor(ossLogE.getUserId());
		ossLogDO.setCreateTime(ossLogE.getUploadTime());
		ossLogDO.setUpdateTime(ossLogE.getUploadTime());
		ossLogDO.setTenantId(ossLogE.getTenantId());
		ossLogDO.setId(ossLogE.getId());
		ossLogDO.setType(ossLogE.getType());
		return ossLogDO;
	}

	public static OssLogE toEntity(OssLogCO ossLogCO) {
		OssLogE ossLogE = OssDomainFactory.getOssLog();
		ossLogE.setName(ossLogCO.getName());
		ossLogE.setMd5(ossLogCO.getMd5());
		ossLogE.setUrl(ossLogCO.getUrl());
		ossLogE.setSize(ossLogCO.getSize());
		ossLogE.setContentType(ossLogCO.getContentType());
		ossLogE.setFormat(ossLogCO.getFormat());
		ossLogE.setOssId(ossLogCO.getOssId());
		ossLogE.setUserId(ossLogCO.getUserId());
		ossLogE.setTenantId(ossLogCO.getTenantId());
		ossLogE.setUploadTime(ossLogCO.getUploadTime());
		ossLogE.setId(ossLogCO.getId());
		ossLogE.setType(ossLogCO.getType());
		return ossLogE;
	}

}
