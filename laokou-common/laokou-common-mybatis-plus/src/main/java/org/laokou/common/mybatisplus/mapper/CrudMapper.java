/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.i18n.dto.PageQuery;

import java.util.List;

/**
 * 增删改查Mapper.
 *
 * @author laokou
 */
public interface CrudMapper<ID, VERSION, DO> extends BaseMapper<DO> {

	VERSION selectVersion(ID id);

	void selectObjectListHandler(@Param(PageQuery.PAGE_QUERY) PageQuery pageQuery, ResultHandler<DO> handler);

	long selectObjectCount(@Param(PageQuery.PAGE_QUERY) PageQuery pageQuery);

	List<DO> selectObjectPage(@Param(PageQuery.PAGE_QUERY) PageQuery pageQuery);

	List<DO> selectObjectList(@Param(PageQuery.PAGE_QUERY) PageQuery pageQuery);

}
