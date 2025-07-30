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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.dubbo.rpc.RpcContext;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.oss.model.BaseOss;
import org.laokou.common.oss.model.FileInfo;
import org.laokou.common.oss.model.StoragePolicyEnum;
import org.laokou.oss.dto.OssUploadCmd;
import org.laokou.oss.dto.clientobject.OssUploadCO;
import org.laokou.oss.dto.domainevent.OssUploadEvent;
import org.laokou.oss.factory.OssDomainFactory;
import org.laokou.oss.gatewayimpl.database.dataobject.OssDO;
import org.laokou.oss.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.oss.model.FileFormatEnum;
import org.laokou.oss.model.OssA;
import org.laokou.oss.model.OssUploadV;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.laokou.common.mybatisplus.mapper.BaseDO.CREATOR;
import static org.laokou.common.mybatisplus.mapper.BaseDO.TENANT_ID;

/**
 * @author laokou
 */
public final class OssConvertor {

	private OssConvertor() {
	}

	public static OssA toEntity(String fileType, long size, String extName, byte[] buffer, String contentType,
			String name) {
		OssA oss = OssDomainFactory.getOss();
		oss.setSize(size);
		oss.setExtName(extName);
		oss.setFileFormatEnum(FileFormatEnum.getByCode(fileType));
		oss.setContentType(contentType);
		oss.setName(name);
		oss.setBuffer(buffer);
		oss.setMd5(DigestUtils.md5DigestAsHex(buffer));
		return oss;
	}

	public static OssUploadCmd toAssembler(org.laokou.oss.api.OssUploadCmd cmd) {
		return new OssUploadCmd(cmd.getFileType(), cmd.getBuffer().toByteArray(), cmd.getName(), cmd.getExtName(),
				cmd.getContentType(), cmd.getSize());
	}

	public static org.laokou.oss.api.OssUploadCO toClientObject(OssUploadCO co) {
		if (ObjectUtils.isNull(co)) {
			return org.laokou.oss.api.OssUploadCO.newBuilder().build();
		}
		return org.laokou.oss.api.OssUploadCO.newBuilder()
			.setId(co.getId())
			.setUrl(co.getUrl())
			.build();
	}

	public static OssUploadCO toClientObject(OssA ossA) {
		OssUploadCO ossUploadCO = new OssUploadCO();
		ossUploadCO.setUrl(ossA.getUrl());
		ossUploadCO.setId(ossA.getId());
		return ossUploadCO;
	}

	public static OssUploadEvent toDomainEvent(OssA ossA) {
		String creator = RpcContext.getServerAttachment().getAttachment(CREATOR);
		String tenantId = RpcContext.getServerAttachment().getAttachment(TENANT_ID);
		FileFormatEnum fileFormatEnum = ossA.getFileFormatEnum();
		return new OssUploadEvent(ossA.getId(), ossA.getName(), ossA.getMd5(), ossA.getUrl(), ossA.getSize(), ossA.getOssId(),
				ossA.getContentType(), ossA.getExtName(), ossA.getCreateTime(), Long.valueOf(tenantId), Long.valueOf(creator), fileFormatEnum.getCode());
	}

	public static FileInfo toFileInfo(byte[] buffer, long size, String contentType, String name, String extName) {
		return new FileInfo(new ByteArrayInputStream(buffer), size, contentType, name, extName);
	}

	public static List<BaseOss> toBaseOssList(List<OssDO> list) {
		return list.stream().map(OssConvertor::toBaseOss).toList();
	}

	public static OssUploadV toValueObject(org.laokou.common.oss.model.OssUploadCO ossUploadCO) {
		return new OssUploadV(ossUploadCO.getUrl(), ossUploadCO.getOssId());
	}

	public static OssUploadV toValueObject(OssLogDO ossLogDO) {
		return new OssUploadV(ossLogDO.getUrl(), ossLogDO.getId());
	}

	private static BaseOss toBaseOss(OssDO ossDO) {
		try {
			return StoragePolicyEnum.getByCode(ossDO.getType()).getOss(ossDO.getId(), ossDO.getName(), ossDO.getParam());
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
