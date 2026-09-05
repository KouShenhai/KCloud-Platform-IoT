/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.admin.dept.convertor.DeptConvertor;
import org.laokou.admin.dept.gateway.DeptGateway;
import org.laokou.admin.dept.gatewayimpl.database.DeptMapper;
import org.laokou.admin.dept.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.dept.model.DeptA;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	private final SqlSessionFactory sqlSessionFactory;

	@Override
	public void createDept(DeptA deptA) {
		DeptDO deptDO = DeptConvertor.toDataObject(deptA);
		updateDeptLevels(deptDO);
		deptMapper.insert(deptDO);
	}

	@Override
	public void updateDept(DeptA deptA) {
		DeptDO deptDO = DeptConvertor.toDataObject(deptA);
		Long id = deptDO.getId();
		Integer level = deptDO.getLevel();
		deptDO.setVersion(deptMapper.selectVersion(deptA.getId()));
		updateDeptLevels(deptDO);
		updateDeptById(deptDO);
		updateDeptChildrenLevels(id, level, deptDO);

	}

	@Override
	public void deleteDept(Long[] ids) {
		deptMapper.deleteByIds(Arrays.asList(ids));
	}

	private void updateDeptLevels(DeptDO deptDO) {
		DeptDO parentDept = deptMapper.selectById(deptDO.getPid());
		rebuildLevel(deptDO, parentDept == null ? DeptConvertor.toDataObject() : parentDept);
	}

	private void updateDeptById(DeptDO deptDO) {
		deptMapper.update(Wrappers.lambdaUpdate(DeptDO.class)
			.set(DeptDO::getPid, deptDO.getPid())
			.set(DeptDO::getSort, deptDO.getSort())
			.set(DeptDO::getName, deptDO.getName())
			.set(DeptDO::getLevel, deptDO.getLevel())
			.set(DeptDO::getLevel1, deptDO.getLevel1())
			.set(DeptDO::getLevel2, deptDO.getLevel2())
			.set(DeptDO::getLevel3, deptDO.getLevel3())
			.set(DeptDO::getLevel4, deptDO.getLevel4())
			.set(DeptDO::getLevel5, deptDO.getLevel5())
			.set(DeptDO::getLevel6, deptDO.getLevel6())
			.set(DeptDO::getLevel7, deptDO.getLevel7())
			.set(DeptDO::getLevel8, deptDO.getLevel8())
			.set(DeptDO::getLevel9, deptDO.getLevel9())
			.set(DeptDO::getVersion, deptDO.getVersion() + 1)
			.eq(DeptDO::getId, deptDO.getId())
			.eq(DeptDO::getVersion, deptDO.getVersion()));
	}

	private void updateDeptChildrenLevels(Long id, Integer level, DeptDO deptDO) {
		List<DeptDO> list = getDeptChildrenList(id, level);
		Map<Long, DeptDO> deptDOMap = new LinkedHashMap<>(Map.of(id, deptDO));
		for (DeptDO dptDO : list) {
			rebuildLevel(dptDO, deptDOMap.get(dptDO.getPid()));
			deptDOMap.put(dptDO.getId(), dptDO);
		}
		updateDeptByIds(list);
	}

	private void updateDeptByIds(List<DeptDO> list) {
		MybatisBatch.Method<DeptDO> mapperMethod = new MybatisBatch.Method<>(DeptMapper.class);
		MybatisBatchUtils.execute(sqlSessionFactory, list,
				mapperMethod.update(item -> Wrappers.lambdaUpdate(DeptDO.class)
					.set(DeptDO::getLevel, item.getLevel())
					.set(DeptDO::getLevel1, item.getLevel1())
					.set(DeptDO::getLevel2, item.getLevel2())
					.set(DeptDO::getLevel3, item.getLevel3())
					.set(DeptDO::getLevel4, item.getLevel4())
					.set(DeptDO::getLevel5, item.getLevel5())
					.set(DeptDO::getLevel6, item.getLevel6())
					.set(DeptDO::getLevel7, item.getLevel7())
					.set(DeptDO::getLevel8, item.getLevel8())
					.set(DeptDO::getLevel9, item.getLevel9())
					.set(DeptDO::getVersion, item.getVersion() + 1)
					.eq(DeptDO::getId, item.getId())
					.eq(DeptDO::getVersion, item.getVersion())));
	}

	private List<DeptDO> getDeptChildrenList(Long id, Integer level) {
		return deptMapper.selectList(Wrappers.lambdaUpdate(DeptDO.class)
			.and(w -> w.eq(DeptDO::getLevel1, id)
				.or()
				.eq(DeptDO::getLevel2, id)
				.or()
				.eq(DeptDO::getLevel3, id)
				.or()
				.eq(DeptDO::getLevel4, id)
				.or()
				.eq(DeptDO::getLevel5, id)
				.or()
				.eq(DeptDO::getLevel6, id)
				.or()
				.eq(DeptDO::getLevel7, id)
				.or()
				.eq(DeptDO::getLevel8, id)
				.or()
				.eq(DeptDO::getLevel9, id))
			.ne(DeptDO::getId, id)
			.orderByAsc(DeptDO::getLevel));
	}

	private void rebuildLevel(DeptDO deptDO, DeptDO parentDeptDO) {
		clearLevels(deptDO);
		int level = parentDeptDO.getLevel() + 1;
		deptDO.setLevel(level);
		copyParentLevels(deptDO, parentDeptDO, level);
		setCurrentLevel(deptDO, level);
	}

	private void clearLevels(DeptDO deptDO) {
		deptDO.setLevel1(null);
		deptDO.setLevel2(null);
		deptDO.setLevel3(null);
		deptDO.setLevel4(null);
		deptDO.setLevel5(null);
		deptDO.setLevel6(null);
		deptDO.setLevel7(null);
		deptDO.setLevel8(null);
		deptDO.setLevel9(null);
	}

	private void copyParentLevels(DeptDO deptDO, DeptDO parentDeptDO, int level) {
		if (level > 1) {
			deptDO.setLevel1(parentDeptDO.getLevel1());
		}
		if (level > 2) {
			deptDO.setLevel2(parentDeptDO.getLevel2());
		}
		if (level > 3) {
			deptDO.setLevel3(parentDeptDO.getLevel3());
		}
		if (level > 4) {
			deptDO.setLevel4(parentDeptDO.getLevel4());
		}
		if (level > 5) {
			deptDO.setLevel5(parentDeptDO.getLevel5());
		}
		if (level > 6) {
			deptDO.setLevel6(parentDeptDO.getLevel6());
		}
		if (level > 7) {
			deptDO.setLevel7(parentDeptDO.getLevel7());
		}
		if (level > 8) {
			deptDO.setLevel8(parentDeptDO.getLevel8());
		}
	}

	private void setCurrentLevel(DeptDO deptDO, int level) {
		Long id = deptDO.getId();
		switch (level) {
			case 1 -> deptDO.setLevel1(id);
			case 2 -> deptDO.setLevel2(id);
			case 3 -> deptDO.setLevel3(id);
			case 4 -> deptDO.setLevel4(id);
			case 5 -> deptDO.setLevel5(id);
			case 6 -> deptDO.setLevel6(id);
			case 7 -> deptDO.setLevel7(id);
			case 8 -> deptDO.setLevel8(id);
			case 9 -> deptDO.setLevel9(id);
			default -> throw new IllegalArgumentException("非法参数");
		}
	}

}
