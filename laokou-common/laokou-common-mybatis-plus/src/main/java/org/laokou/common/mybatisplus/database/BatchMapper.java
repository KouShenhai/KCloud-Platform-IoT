/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * @author laokou
 */
public interface BatchMapper<T> extends BaseMapper<T> {

	/**
	 * slf4j日志配置.
	 */
	Logger LOG = LoggerFactory.getLogger(BatchMapper.class);

	/**
	 * 新增一条数据.
	 * @param entity 一条模型数据
	 * @return 新增结果
	 */
	int save(T entity);

	/**
	 * 获取版本号.
	 * @param id ID
	 * @param clazz 类型
	 * @return int
	 */
	default int getVersion(Long id, Class<T> clazz) {
		T value = this.selectOne(Wrappers.query(clazz).eq("id", id).select("version"));
		if (ObjectUtil.isNull(value)) {
			throw new SystemException("数据不存在");
		}
		return 0;
		//return value.getVersion();
	}

	/**
	 * 查询列表（游标）.
	 * @param tables 表集合
	 * @param param 参数
	 * @param handler 回调处理器
	 * @param pageQuery 分页查询参数
	 */
	void resultListFilter(@Param("tables") List<String> tables, @Param("param") T param, ResultHandler<T> handler,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 查看总数（游标）.
	 * @param tables 表集合
	 * @param param 参数
	 * @param pageQuery 分页查询参数
	 * @return 总数
	 */
	Integer resultCountFilter(@Param("tables") List<String> tables, @Param("param") T param,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 新增一条数据（动态生成雪花算法ID）.
	 * @param t 一条模型数据
	 * @return 新增结果
	 */
	default Boolean insertTable(T t) {
		try {
			//t.setId(IdGenerator.defaultSnowflakeId());
			return this.insert(t) > 0;
		}
		catch (Exception e) {
			LOG.error("错误信息", e);
			return false;
		}
	}

}
