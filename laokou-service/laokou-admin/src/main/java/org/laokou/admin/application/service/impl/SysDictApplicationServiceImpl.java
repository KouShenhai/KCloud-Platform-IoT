/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package org.laokou.admin.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.laokou.admin.application.service.SysDictApplicationService;
import org.laokou.admin.domain.sys.entity.SysDictDO;
import org.laokou.admin.domain.sys.repository.service.SysDictService;
import org.laokou.admin.domain.sys.repository.service.SysUserService;
import org.laokou.admin.interfaces.qo.SysDictQO;
import org.laokou.admin.interfaces.vo.SysDictVO;
import org.laokou.common.user.SecurityUser;
import org.laokou.admin.interfaces.dto.SysDictDTO;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.ConvertUtil;
import org.laokou.datasource.annotation.DataFilter;
import org.laokou.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SysDictApplicationServiceImpl implements SysDictApplicationService {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_dict")
    public IPage<SysDictVO> queryDictPage(SysDictQO qo) {
        IPage<SysDictVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysDictService.getDictList(page,qo);
    }

    @Override
    @DataSource("master")
    public SysDictVO getDictById(Long id) {
        return sysDictService.getDictById(id);
    }

    @Override
    @DataSource("master")
    public Boolean insertDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setCreator(SecurityUser.getUserId(request));
        final UserDetail userDetail = sysUserService.getUserDetail(dictDO.getCreator());
        dictDO.setDeptId(userDetail.getDeptId());
        return sysDictService.save(dictDO);
    }

    @Override
    @DataSource("master")
    public Boolean updateDict(SysDictDTO dto, HttpServletRequest request) {
        SysDictDO dictDO = ConvertUtil.sourceToTarget(dto, SysDictDO.class);
        dictDO.setEditor(SecurityUser.getUserId(request));
        return sysDictService.updateById(dictDO);
    }

    @Override
    @DataSource("master")
    public Boolean deleteDict(Long id) {
        sysDictService.deleteDict(id);
        return true;
    }
}
