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
import org.laokou.admin.vo.SysDictVO;
import org.laokou.admin.server.domain.sys.entity.SysDictDO;
import org.laokou.admin.server.interfaces.qo.SysDictQo;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface SysDictMapper extends BatchMapper<SysDictDO> {

	/**
	 * 分页查询字典
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysDictVO> getDictList(IPage<SysDictVO> page, @Param("qo") SysDictQo qo);

	/**
	 * 下拉框选项
	 * @param type
	 * @return
	 */
	List<OptionVO> getOptionList(@Param("type") String type);

	/**
	 * 根据id查询字典
	 * @param id
	 * @return
	 */
	SysDictVO getDictById(@Param("id") Long id);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(@Param("id") Long id);

}
