/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AbstractDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.context.DynamicTableSuffixContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * @author laokou
 */
public interface BatchMapper<T extends AbstractDO> extends BaseMapper<T> {

	Logger log = LoggerFactory.getLogger(BatchMapper.class);

	int save(T entity);

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
		if (Objects.isNull(value)) {
			throw new SystemException("数据不存在");
		}
		return value.getVersion();
	}

	void resultListFilter(@Param("tables") List<String> tables, @Param("param") T param, ResultHandler<T> handler,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	Integer resultCountFilter(@Param("tables") List<String> tables, @Param("param") T param,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	@Update("${sql}")
	void execute(@Param("sql") String sql);

	/**
	 * 新增动态分表
	 */
	default void insertDynamicTable(T t, String sql, String suffix) {
		try {
			DynamicTableSuffixContextHolder.set(suffix);
			t.setId(IdGenerator.defaultSnowflakeId());
			this.insert(t);
		}
		catch (Exception e) {
			log.error("错误信息", e);
			this.execute(sql);
			this.insert(t);
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

	default Integer getDynamicVersion(Long id, Class<T> clazz, String suffix) {
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
			log.error("错误信息", e);
			return false;
		}
	}

}
