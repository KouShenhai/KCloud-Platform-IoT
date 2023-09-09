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
import org.laokou.admin.dto.SysUserDTO;
import org.laokou.admin.vo.SysUserVO;
import org.laokou.admin.server.domain.sys.entity.SysUserDO;
import org.laokou.admin.server.interfaces.qo.SysUserQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户类
 *
 * @author laokou
 */
@Mapper
@Repository
public interface SysUserMapper extends BatchMapper<SysUserDO> {

	/**
	 * 分页查询用户
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, @Param("qo") SysUserQo qo);

	/**
	 * 更新用户
	 * @param dto
	 */
	void updateUser(@Param("dto") SysUserDTO dto);

	/**
	 * 获取下拉用户选择列表
	 * @param tenantId
	 * @return
	 */
	List<OptionVO> getOptionList(Long tenantId);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(@Param("id") Long id);

}
