/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.common.mybatisplus.database;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.context.DynamicTableSuffixContextHolder;
import org.laokou.common.mybatisplus.database.dataobject.BaseDO;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface BatchMapper<T extends BaseDO> extends BaseMapper<T> {

	/**
	 * 批量插入
	 * @param list 数据集
	 */
	void insertBatch(@Param("list") List<T> list);

	/**
	 * 批量插入
	 * @param entityList 数据集
	 * @return int
	 */
	int insertBatchSomeColumn(List<T> entityList);

	int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

	int deleteByIdWithFill(T entity);

	/**
	 * 获取版本号
	 * @param id ID
	 * @param clazz 类型
	 * @return int
	 */
	default int getVersion(Long id, Class<T> clazz) {
		T value = this.selectOne(Wrappers.query(clazz).eq("id", id).select("version"));
		if (value == null) {
			throw new GlobalException("数据不存在");
		}
		return value.getVersion();
	}

	void resultListFilter(@Param("tables") List<String> tables, @Param("param") T param, ResultHandler<T> handler,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	@Update("${sql}")
	void execute(@Param("sql") String sql);

	/**
	 * 新增动态分表
	 */
	default Boolean insertDynamicTable(T t, String sql, String suffix) {
		try {
			DynamicTableSuffixContextHolder.set(suffix);
			t.setId(IdGenerator.defaultSnowflakeId());
			return this.insert(t) > 0;
		}
		catch (Exception e) {
			this.execute(sql);
			return this.insert(t) > 0;
		}
		finally {
			DynamicTableSuffixContextHolder.clear();
		}
	}

	default Integer deleteDynamicTableById(Long id, String suffix) {
		try {
			DynamicTableSuffixContextHolder.set(suffix);
			return this.deleteById(id);
		}
		finally {
			DynamicTableSuffixContextHolder.clear();
		}
	}

	default Integer getDynamicTableVersion(Long id, Class<T> clazz, String suffix) {
		try {
			DynamicTableSuffixContextHolder.set(suffix);
			return getVersion(id, clazz);
		}
		finally {
			DynamicTableSuffixContextHolder.clear();
		}
	}

	default T getDynamicTableById(Class<T> clazz, Long id, String suffix, String... columns) {
		try {
			DynamicTableSuffixContextHolder.set(suffix);
			return this.selectOne(Wrappers.query(clazz).eq("id", id).select(columns));
		}
		finally {
			DynamicTableSuffixContextHolder.clear();
		}
	}

	default Boolean insertTable(T t) {
		try {
			t.setId(IdGenerator.defaultSnowflakeId());
			return this.insert(t) > 0;
		}
		catch (Exception e) {
			return false;
		}
	}

}
