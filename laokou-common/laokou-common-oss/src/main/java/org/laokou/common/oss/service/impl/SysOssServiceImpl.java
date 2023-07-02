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
package org.laokou.common.oss.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.oss.entity.SysOssDO;
import org.laokou.common.oss.mapper.SysOssMapper;
import org.laokou.common.oss.qo.SysOssQo;
import org.laokou.common.oss.service.SysOssService;
import org.laokou.common.oss.vo.SysOssVO;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysOssServiceImpl extends ServiceImpl<SysOssMapper, SysOssDO> implements SysOssService {

	private final SysOssMapper sysOssMapper;

	@Override
	@DS(Constant.TENANT)
	public SysOssVO queryOssConfig() {
		return sysOssMapper.queryOssConfig();
	}

	@Override
	public Boolean deleteOss(Long id) {
		this.baseMapper.deleteById(id);
		return true;
	}

	@Override
	public Integer getVersion(Long id) {
		return this.baseMapper.getVersion(id);
	}

	@Override
	public IPage<SysOssVO> queryOssPage(IPage<SysOssVO> page, SysOssQo qo) {
		return this.baseMapper.queryOssPage(page, qo);
	}

	@Override
	public SysOssVO getOssById(Long id) {
		return this.baseMapper.getOssById(id);
	}

}
