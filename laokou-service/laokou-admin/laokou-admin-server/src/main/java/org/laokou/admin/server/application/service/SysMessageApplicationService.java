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
package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.MessageDTO;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;

/**
 * @author laokou
 */
public interface SysMessageApplicationService {

	/**
	 * 新增消息
	 * @param dto
	 * @return
	 */
	Boolean insertMessage(MessageDTO dto);

	/**
	 * 分页查询消息
	 * @param qo
	 * @return
	 */
	IPage<SysMessageVO> queryMessagePage(SysMessageQo qo);

	/**
	 * 根据详情id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageByDetailId(Long id);

	/**
	 * 根据id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageById(Long id);

	/**
	 * 分页查询未读消息
	 * @param qo
	 * @return
	 */
	IPage<SysMessageVO> getUnReadList(SysMessageQo qo);

	/**
	 * 未读消息条数
	 * @return
	 */
	Long unReadCount();

	/**
	 * 推送消息
	 * @param dto
	 * @return
	 */
	Boolean pushMessage(MessageDTO dto);

}
