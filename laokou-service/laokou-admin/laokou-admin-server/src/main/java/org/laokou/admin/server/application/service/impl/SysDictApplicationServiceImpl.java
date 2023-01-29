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
package org.laokou.admin.server.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysDictApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysDictDO;
import org.laokou.admin.server.domain.sys.repository.service.SysDictService;
import org.laokou.admin.server.interfaces.qo.SysDictQo;
import org.laokou.admin.client.vo.SysDictVO;
import org.laokou.admin.client.dto.SysDictDTO;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.data.filter.annotation.DataFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysDictApplicationServiceImpl implements SysDictApplicationService {

    private final SysDictService sysDictService;

    @Override
    @DataFilter(tableAlias = "boot_sys_dict")
    public IPage<SysDictVO> queryDictPage(SysDictQo qo) {
        IPage<SysDictVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysDictService.getDictList(page,qo);
    }

    @Override
    public SysDictVO getDictById(Long id) {
        return sysDictService.getDictById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertDict(SysDictDTO dto) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setCreator(UserUtil.getUserId());
        dictDO.setDeptId(UserUtil.getDeptId());
        return sysDictService.save(dictDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDict(SysDictDTO dto) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setEditor(UserUtil.getUserId());
        return sysDictService.updateById(dictDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteDict(Long id) {
        sysDictService.deleteDict(id);
        return true;
    }
}
