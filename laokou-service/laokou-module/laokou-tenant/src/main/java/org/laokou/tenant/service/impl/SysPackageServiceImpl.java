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
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.mapper.SysPackageMapper;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.service.SysPackageService;
import org.laokou.tenant.vo.SysPackageVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
public class SysPackageServiceImpl extends ServiceImpl<SysPackageMapper, SysPackageDO> implements SysPackageService {

    @Override
    public Integer getVersion(Long id) {
        return this.baseMapper.getVersion(id);
    }

    @Override
    public Boolean deletePackage(Long id) {
        this.baseMapper.deleteById(id);
        return true;
    }

    @Override
    public IPage<SysPackageVO> queryPackagePage(IPage<SysPackageVO> page,SysPackageQo qo) {
        return this.baseMapper.queryPackagePage(page,qo);
    }

    @Override
    public SysPackageVO getPackageById(Long id) {
        return this.baseMapper.getPackageById(id);
    }

    @Override
    public List<OptionVO> getOptionList() {
        return this.baseMapper.getOptionList();
    }
}
