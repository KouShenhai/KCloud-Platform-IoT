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
package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;

/**
 * @author laokou
 */
public interface SysMessageService extends IService<SysMessageDO> {

	/**
	 * 分页查询消息
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysMessageVO> getMessageList(IPage<SysMessageVO> page, SysMessageQo qo);

	/**
	 * 根据详情id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageByDetailId(Long id);

	/**
	 * 分页查询未读消息
	 * @param page
	 * @param userId
	 * @param type
	 * @return
	 */
	IPage<SysMessageVO> getUnReadList(IPage<SysMessageVO> page, Integer type, Long userId);

	/**
	 * 消息读取
	 * @param version
	 * @param id
	 */
	void readMessage(Long id, Integer version);

	/**
	 * 根据id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageById(Long id);

}
