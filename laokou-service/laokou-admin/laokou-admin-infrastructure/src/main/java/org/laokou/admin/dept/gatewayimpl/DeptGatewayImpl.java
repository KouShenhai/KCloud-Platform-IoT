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

package org.laokou.admin.dept.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dept.convertor.DeptConvertor;
import org.laokou.admin.dept.gateway.DeptGateway;
import org.laokou.admin.dept.gatewayimpl.database.DeptMapper;
import org.laokou.admin.dept.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 部门网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	private final TransactionalUtil transactionalUtil;

	public void create(DeptE deptE) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.insert(DeptConvertor.toDataObject(deptE));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("新增失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	public void update(DeptE deptE) {
		DeptDO deptDO = DeptConvertor.toDataObject(deptE);
		deptDO.setVersion(deptMapper.selectVersion(deptE.getId()));
		update(deptDO);
	}

	public void delete(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("删除失败，错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new RuntimeException(msg);
			}
		});
	}

	private void update(DeptDO deptDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.updateById(deptDO);
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
