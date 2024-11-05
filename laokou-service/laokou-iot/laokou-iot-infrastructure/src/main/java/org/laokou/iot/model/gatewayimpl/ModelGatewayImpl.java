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

package org.laokou.iot.model.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.model.model.ModelE;
import org.springframework.stereotype.Component;
import org.laokou.iot.model.gateway.ModelGateway;
import org.laokou.iot.model.gatewayimpl.database.ModelMapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.iot.model.convertor.ModelConvertor;
import org.laokou.iot.model.gatewayimpl.database.dataobject.ModelDO;

/**
*
* 模型网关实现.
*
* @author laokou
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class ModelGatewayImpl implements ModelGateway {

	private final ModelMapper modelMapper;
	private final TransactionalUtil transactionalUtil;

	public void create(ModelE modelE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				modelMapper.insert(ModelConvertor.toDataObject(modelE, true));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(ModelE modelE) {
		ModelDO modelDO = ModelConvertor.toDataObject(modelE, false);
		modelDO.setVersion(modelMapper.selectVersion(modelE.getId()));
		update(modelDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				modelMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(ModelDO modelDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				modelMapper.updateById(modelDO);
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("修改失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

}
