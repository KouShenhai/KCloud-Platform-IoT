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

package org.laokou.common.tenant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户数据库映射.
 *
 * @author laokou
 */
@Mapper
@Repository
public interface TenantMapper extends CrudMapper<Long, Integer, TenantDO> {

	List<TenantDO> selectPageByCondition(@Param("pageQuery") PageQuery pageQuery);

	long selectCountByCondition(@Param("pageQuery") PageQuery pageQuery);

	Long selectIdTenantCode(@Param("tenantCode") String tenantCode);

}
