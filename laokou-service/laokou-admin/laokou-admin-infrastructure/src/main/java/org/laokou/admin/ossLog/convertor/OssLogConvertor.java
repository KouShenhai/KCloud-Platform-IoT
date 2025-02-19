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
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * OSS日志转换器.
 *
 * @author laokou
 */
public class OssLogConvertor {

	public static OssLogDO toDataObject(OssLogE ossLogE) {
		OssLogDO ossLogDO = ConvertUtil.sourceToTarget(ossLogE, OssLogDO.class);
		if (ObjectUtil.isNull(ossLogDO.getId())) {
			ossLogDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return ossLogDO;
	}

	public static OssLogCO toClientObject(OssLogDO ossLogDO) {
		return ConvertUtil.sourceToTarget(ossLogDO, OssLogCO.class);
	}

	public static OssLogE toEntity(OssLogCO ossLogCO) {
		return ConvertUtil.sourceToTarget(ossLogCO, OssLogE.class);
	}

}
