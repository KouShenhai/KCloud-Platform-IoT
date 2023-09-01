/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;
import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface SysMessageMapper extends BatchMapper<SysMessageDO> {

	/**
	 * 分页查询消息
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, @Param("qo") SysMessageQo qo);

	/**
	 * 根据detail查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageByDetailId(@Param("id") Long id);

	/**
	 * 分页查询未读消息
	 * @param page
	 * @param userId
	 * @param type
	 * @return
	 */
	IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page, @Param("type") Integer type,
			@Param("userId") Long userId);

	/**
	 * 消息读取
	 * @param id
	 * @param version
	 * @param userId
	 */
	void readMessage(@Param("id") Long id, @Param("version") Integer version, @Param("userId") Long userId);

	/**
	 * 根据id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageById(@Param("id") Long id);

}
