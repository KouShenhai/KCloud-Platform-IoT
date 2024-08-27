// @formatter:off
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

package ${packageName}.${instanceName}.gatewayimpl;

import lombok.RequiredArgsConstructor;
import ${packageName}.${instanceName}.model.${className}E;
import org.springframework.stereotype.Component;
import ${packageName}.${instanceName}.gateway.${className}Gateway;
import ${packageName}.${instanceName}.gatewayimpl.database.${className}Mapper;
import java.util.Arrays;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import ${packageName}.${instanceName}.convertor.${className}Convertor;
import ${packageName}.${instanceName}.gatewayimpl.database.dataobject.${className}DO;

/**
*
* ${comment}网关实现.
*
* @author ${author}
*/
@Slf4j
@Component
@RequiredArgsConstructor
public class ${className}GatewayImpl implements ${className}Gateway {

	private final ${className}Mapper ${instanceName}Mapper;
	private final TransactionalUtil transactionalUtil;

	public void create(${className}E ${instanceName}E) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				${instanceName}Mapper.insert(${className}Convertor.toDataObject(${instanceName}E, true));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(${className}E ${instanceName}E) {
		${className}DO ${instanceName}DO = ${className}Convertor.toDataObject(${instanceName}E, false);
		${instanceName}DO.setVersion(${instanceName}Mapper.selectVersion(${instanceName}E.getId()));
		update(${instanceName}DO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				${instanceName}Mapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(${className}DO ${instanceName}DO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				${instanceName}Mapper.updateById(${instanceName}DO);
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
// @formatter:on
