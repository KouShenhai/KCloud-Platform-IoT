/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.tenant.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.tenant.entity.SysTenantDO;
import org.laokou.tenant.mapper.SysTenantMapper;
import org.laokou.tenant.qo.SysTenantQo;
import org.laokou.tenant.service.SysTenantService;
import org.laokou.tenant.vo.SysTenantVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenantDO> implements SysTenantService {

    @Override
    public List<OptionVO> getOptionList() {
        return this.baseMapper.getOptionList();
    }

    @Override
    public Integer getVersion(Long id) {
        return this.baseMapper.getVersion(id);
    }

    @Override
    public IPage<SysTenantVO> queryTenantPage(IPage<SysTenantVO> page, SysTenantQo qo) {
        return this.baseMapper.queryTenantPage(page,qo);
    }

    @Override
    public SysTenantVO getTenantById(Long id) {
        return this.baseMapper.getTenantById(id);
    }

    @Override
    public void deleteTenant(Long id) {
        this.baseMapper.deleteById(id);
    }
}
