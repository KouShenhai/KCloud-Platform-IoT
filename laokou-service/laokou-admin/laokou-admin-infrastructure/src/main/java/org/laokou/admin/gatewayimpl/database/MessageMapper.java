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

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.MessageDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import static org.laokou.common.i18n.common.MybatisPlusConstants.USER_ID;
import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 消息.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface MessageMapper extends BatchMapper<MessageDO> {

	/**
	 * 根据用户ID和Type查询未读消息列表.
	 * @param page 分页参数
	 * @param userId 用户ID
	 * @param type 类型
	 * @return 未读消息列表
	 */
	IPage<MessageDO> getUnreadMessageListByUserIdAndType(IPage<MessageDO> page, @Param(USER_ID) Long userId,
			@Param("type") Integer type);

	/**
	 * 查询消息列表.
	 * @param page 分页参数
	 * @param title 消息标题
	 * @param pageQuery 分页参数
	 * @return 消息列表
	 */
	IPage<MessageDO> getMessageListFilter(IPage<MessageDO> page, @Param("title") String title,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 根据详情ID查看消息.
	 * @param detailId 详情ID
	 * @return 消息
	 */
	MessageDO getMessageByDetailId(@Param("detailId") Long detailId);

}
