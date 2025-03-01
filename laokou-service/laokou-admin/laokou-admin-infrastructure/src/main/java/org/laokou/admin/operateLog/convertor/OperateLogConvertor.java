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

package org.laokou.admin.operateLog.convertor;

import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.admin.operateLog.model.OperateLogE;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.log.database.dataobject.OperateLogDO;

/**
 * 操作日志转换器.
 *
 * @author laokou
 */
public class OperateLogConvertor {

	public static OperateLogDO toDataObject(OperateLogE operateLogE) {
		OperateLogDO operateLogDO = ConvertUtil.sourceToTarget(operateLogE, OperateLogDO.class);
		if (ObjectUtil.isNull(operateLogDO.getId())) {
			operateLogDO.setId(IdGenerator.defaultSnowflakeId());
		}
		return operateLogDO;
	}

	public static OperateLogCO toClientObject(OperateLogDO operateLogDO) {
		return ConvertUtil.sourceToTarget(operateLogDO, OperateLogCO.class);
	}

	public static OperateLogE toEntity(OperateLogCO operateLogCO) {
		return ConvertUtil.sourceToTarget(operateLogCO, OperateLogE.class);
	}

}
