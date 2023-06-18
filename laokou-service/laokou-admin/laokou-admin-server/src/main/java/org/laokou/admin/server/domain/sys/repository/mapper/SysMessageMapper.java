/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.server.domain.sys.entity.SysMessageDO;
import org.laokou.admin.server.interfaces.qo.SysMessageQo;
import org.laokou.admin.client.vo.MessageDetailVO;
import org.laokou.admin.client.vo.SysMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface SysMessageMapper extends BaseMapper<SysMessageDO> {

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
	 */
	void readMessage(@Param("id") Long id, @Param("version") Integer version);

	/**
	 * 根据id查询消息
	 * @param id
	 * @return
	 */
	MessageDetailVO getMessageById(@Param("id") Long id);

}
