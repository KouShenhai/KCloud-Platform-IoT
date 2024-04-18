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

package org.laokou.flowable.gatewayimpl.database;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.flowable.gatewayimpl.database.dataobject.TaskDO;
import org.springframework.stereotype.Repository;

import static org.laokou.common.i18n.common.MybatisPlusConstants.USER_ID;

/**
 * 任务流程.
 *
 * @author laokou
 */
@Mapper
@Repository
public interface TaskMapper {

	/**
	 * 根据租户ID查看执行人员.
	 * @param instanceId 实例ID
	 * @param tenantId 租户ID
	 * @return 执行人员
	 */
	String getAssigneeByInstanceId(@Param("instanceId") String instanceId, @Param("tenantId") Long tenantId);

	/**
	 * 查询任务流程列表.
	 * @param page 分页参数
	 * @param key key
	 * @param userId 用户ID
	 * @param name 流程名称
	 * @param tenantId 租户ID
	 * @return 任务流程列表
	 */
	IPage<TaskDO> getTaskList(IPage<TaskDO> page, @Param("key") String key, @Param(USER_ID) Long userId,
			@Param("name") String name, @Param("tenantId") Long tenantId);

}
