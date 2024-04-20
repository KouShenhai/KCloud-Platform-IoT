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

package org.laokou.admin.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 字典.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface DictMapper extends CrudMapper<Long, Integer, DictDO> {

	List<DictDO> selectListByCondition(@Param("dict") DictDO dict, @Param(PAGE_QUERY) PageQuery pageQuery);

	long selectCountByCondition(@Param("dict") DictDO dict, @Param(PAGE_QUERY) PageQuery pageQuery);

}
