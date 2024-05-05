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

package org.laokou.common.domain.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.laokou.common.domain.database.dataobject.DomainEventDO;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface DomainEventMapper extends CrudMapper<Long, Integer, DomainEventDO> {

	long selectTotal(@Param("sourceNames") Set<String> sourceNames, @Param("appName") String appName);

	int deleteByYmd(@Param("ymd") String ymd);

	void selectObjects(@Param("sourceNames") Set<String> sourceNames, @Param("appName") String appName,
			ResultHandler<DomainEventDO> resultHandler);

	void updateStatus(@Param("evt") DomainEventDO evt);

}
