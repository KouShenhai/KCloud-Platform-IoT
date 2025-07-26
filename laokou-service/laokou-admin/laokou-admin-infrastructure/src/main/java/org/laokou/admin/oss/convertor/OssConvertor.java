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

package org.laokou.admin.oss.convertor;

import com.google.protobuf.ByteString;
import org.laokou.admin.oss.dto.clientobject.OssUploadCO;
import org.laokou.admin.oss.factory.OssDomainFactory;
import org.laokou.admin.oss.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.core.util.FileUtils;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.admin.oss.model.OssE;
import org.laokou.oss.api.OssUploadCmd;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * OSS转换器.
 *
 * @author laokou
 */
public class OssConvertor {

	public static OssDO toDataObject(OssE ossE) {
		return ConvertUtils.sourceToTarget(ossE, OssDO.class);
	}

	public static OssCO toClientObject(OssDO ossDO) {
		OssCO ossCO = new OssCO();
		ossCO.setId(ossDO.getId());
		ossCO.setName(ossDO.getName());
		ossCO.setType(ossDO.getType());
		ossCO.setParam(ossDO.getParam());
		ossCO.setStatus(ossDO.getStatus());
		ossCO.setCreateTime(ossDO.getCreateTime());
		return ossCO;
	}

	public static OssUploadCO toClientObject(org.laokou.oss.api.OssUploadCO ossUploadCO) {
		OssUploadCO co = new OssUploadCO();
		co.setLogId(ossUploadCO.getId());
		co.setUrl(ossUploadCO.getUrl());
		return co;
	}

	public static OssUploadCmd toAssembler(MultipartFile file, String fileType) throws IOException {
		String name = file.getOriginalFilename();
		Assert.notNull(name, "File name must not be null");
		String extName = FileUtils.getFileExt(name);
		return OssUploadCmd.newBuilder()
				.setBuffer(ByteString.copyFrom(file.getBytes()))
				.setName(UUIDGenerator.generateUUID() + extName)
				.setExtName(extName)
				.setContentType(file.getContentType())
				.setSize(file.getSize())
				.setFileType(fileType)
				.build();
	}

	public static List<OssCO> toClientObjects(List<OssDO> list) {
		return list.stream().map(OssConvertor::toClientObject).toList();
	}

	public static OssE toEntity(OssCO ossCO) {
		OssE ossE = OssDomainFactory.getOss();
		ossE.setId(ossCO.getId());
		ossE.setName(ossCO.getName());
		ossE.setType(ossCO.getType());
		ossE.setParam(ossCO.getParam());
		ossE.setStatus(ossCO.getStatus());
		return ossE;
	}

}
