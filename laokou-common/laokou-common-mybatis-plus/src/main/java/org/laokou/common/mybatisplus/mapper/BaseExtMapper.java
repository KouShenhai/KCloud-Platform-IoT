/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author laokou
 */
@Schema(name = "BaseExtMapper", description = "扩展Mapper")
public interface BaseExtMapper<T> extends BaseMapper<T> {

	/**
	 * 批量插入.
	 * @param entityList 对象集合
	 * @return int
	 */
	int insertBatchSomeColumn(List<T> entityList);

	/**
	 * 根据ID修改某些列.
	 * @param entity 对象
	 * @return 修改结果
	 */
	int alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) T entity);

	/**
	 * 根据ID删除.
	 * @param entity 对象
	 * @return 删除结果
	 */
	int deleteByIdWithFill(T entity);

}
