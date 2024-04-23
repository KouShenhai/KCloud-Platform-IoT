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

package org.laokou.common.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.i18n.dto.PageQuery;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * @author laokou
 */
@Schema(name = "CrudMapper", description = "增删改查Mapper")
public interface CrudMapper<ID, VERSION, DO> extends BaseMapper<DO> {

	/**
	 * 查看版本号.
	 * @param id ID
	 * @return 版本号
	 */
	VERSION selectVersion(ID id);

	/**
	 * 插入数据.
	 * @param entity 插入对象
	 */
	void insertOne(DO entity);

	void selectObjList(@Param("tables") List<String> tables, @Param("param") DO param,
			@Param(PAGE_QUERY) PageQuery pageQuery, ResultHandler<DO> handler);

	long selectObjCount(@Param("tables") List<String> tables, @Param("param") DO param,
			@Param(PAGE_QUERY) PageQuery pageQuery);

}
