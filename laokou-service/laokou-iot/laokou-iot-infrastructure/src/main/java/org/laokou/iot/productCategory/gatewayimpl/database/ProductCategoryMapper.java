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

package org.laokou.iot.productCategory.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.laokou.iot.productCategory.gatewayimpl.database.dataobject.ProductCategoryDO;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.laokou.common.i18n.dto.PageQuery;

/**
 *
 * 产品类别数据库映射.
 *
 * @author laokou
 */
@Mapper
@Repository
public interface ProductCategoryMapper extends CrudMapper<Long, Integer, ProductCategoryDO> {

	List<ProductCategoryDO> selectObjectPage(@Param("pageQuery") PageQuery pageQuery);

}
