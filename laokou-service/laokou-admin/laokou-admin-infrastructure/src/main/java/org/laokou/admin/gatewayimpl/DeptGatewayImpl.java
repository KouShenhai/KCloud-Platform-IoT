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

package org.laokou.admin.gatewayimpl;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.laokou.common.i18n.common.constant.StringConstant.COMMA;

/**
 * 部门管理.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	private final TransactionalUtil transactionalUtil;

	private final DeptConvertor deptConvertor;

	private final MybatisUtil mybatisUtil;

	/**
	 * 新增部门.
	 * @param dept 部门对象
	 */
	@Override
	public void create(Dept dept) {
		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, dept.getName()));
		dept.checkName(count);
		DeptDO deptDO = deptConvertor.toDataObject(dept);
		// 修改新path
		deptDO.setPath(getNewPath(deptDO));
		create(deptDO);
	}

	/**
	 * 修改部门.
	 * @param dept 部门对象
	 */
	@Override
	public void modify(Dept dept) {
		// dept.checkNullId();
		long count = deptMapper.selectCount(
				Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, dept.getName()).ne(DeptDO::getId, dept.getId()));
		dept.checkName(count);
		dept.checkIdAndPid();
		DeptDO deptDO = deptConvertor.toDataObject(dept);
		// 修改新path
		deptDO.setPath(getNewPath(deptDO));
		String oldPath = deptMapper.selectPathById(deptDO.getId());
		// 根据oldPath获取所有子节点
		List<DeptDO> children = deptMapper.selectListByPath(oldPath);
		// 版本号
		deptDO.setVersion(deptMapper.selectVersion(deptDO.getId()));
		modify(deptDO, oldPath, deptDO.getPath(), children);
	}

	/**
	 * 根据ID删除部门.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.deleteBatchIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改部门.
	 * @param deptDO 部门数据模型
	 * @param oldPath 旧部门PATH
	 * @param newPath 新部门PATH
	 * @param children 部门子节点列表
	 */
	public void modify(DeptDO deptDO, String oldPath, String newPath, List<DeptDO> children) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.updateById(deptDO);
				// 修改PATH
				modifyPath(oldPath, newPath, children);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增部门.
	 * @param deptDO 部门数据模型
	 */
	private void create(DeptDO deptDO) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				deptMapper.insert(deptDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改部门子节点PATH.
	 * @param oldPath 旧部门PATH
	 * @param newPath 新部门PATH
	 * @param children 部门子节点列表
	 */
	private void modifyPath(String oldPath, String newPath, List<DeptDO> children) {
		if (CollectionUtil.isNotEmpty(children)) {
			children.parallelStream().forEach(item -> item.setPath(item.getPath().replace(oldPath, newPath)));
			mybatisUtil.batch(children, DeptMapper.class, DynamicDataSourceContextHolder.peek(),
					DeptMapper::updateById);
		}
	}

	/**
	 * 查看部门PATH.
	 * @param deptDO 部门对象
	 * @return 部门PATH
	 */
	private String getNewPath(DeptDO deptDO) {
		Long id = deptDO.getId();
		Long pid = deptDO.getPid();
		String path = deptMapper.selectPathById(pid);
		return StringUtil.isNotEmpty(path) ? path + COMMA + id : 0 + COMMA + id;
	}

}
