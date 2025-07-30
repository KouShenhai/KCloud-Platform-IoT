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

package org.laokou.admin.ossLog.convertor;

import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
import org.laokou.admin.ossLog.gatewayimpl.database.dataobject.OssLogDO;
import org.laokou.admin.ossLog.model.OssLogE;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.i18n.util.ObjectUtils;

import java.util.List;

/**
 * OSS日志转换器.
 *
 * @author laokou
 */
public class OssLogConvertor {

	public static OssLogDO toDataObject(OssLogE ossLogE) {
		OssLogDO ossLogDO = ConvertUtils.sourceToTarget(ossLogE, OssLogDO.class);
		if (ObjectUtils.isNull(ossLogDO.getId())) {
			ossLogDO.setId(1L);
		}
		return ossLogDO;
	}

	public static List<OssLogCO> toClientObjects(List<OssLogDO> list) {
		return list.stream().map(OssLogConvertor::toClientObject).toList();
	}

	public static OssLogCO toClientObject(OssLogDO ossLogDO) {
		OssLogCO ossLogCO = new OssLogCO();
		ossLogCO.setId(ossLogDO.getId());
		ossLogCO.setName(ossLogDO.getName());
		ossLogCO.setMd5(ossLogDO.getMd5());
		ossLogCO.setUrl(ossLogDO.getUrl());
		ossLogCO.setSize(ossLogDO.getSize());
		ossLogCO.setContentType(ossLogDO.getContentType());
		ossLogCO.setFormat(ossLogDO.getFormat());
		ossLogCO.setOssId(ossLogDO.getOssId());
		ossLogCO.setCreateTime(ossLogDO.getCreateTime());
		ossLogCO.setType(ossLogDO.getType());
		return ossLogCO;
	}

	public static OssLogE toEntity(OssLogCO ossLogCO) {
		return ConvertUtils.sourceToTarget(ossLogCO, OssLogE.class);
	}

}
