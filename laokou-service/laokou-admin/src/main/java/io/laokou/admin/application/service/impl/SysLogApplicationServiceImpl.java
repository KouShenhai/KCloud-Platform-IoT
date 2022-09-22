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
package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.laokou.admin.application.service.SysLogApplicationService;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.domain.sys.repository.service.SysLoginLogService;
import io.laokou.admin.domain.sys.repository.service.SysOperateLogService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.qo.SysOperateLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;
import io.laokou.admin.interfaces.vo.SysOperateLogVO;
import io.laokou.common.dto.LoginLogDTO;
import io.laokou.common.dto.OperateLogDTO;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.datasource.annotation.DataFilter;
import io.laokou.datasource.annotation.DataSource;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@GlobalTransactional(rollbackFor = Exception.class)
public class SysLogApplicationServiceImpl implements SysLogApplicationService {

    @Autowired
    private SysOperateLogService sysOperateLogService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    public Boolean insertOperateLog(OperateLogDTO dto) {
        SysOperateLogDO logDO = ConvertUtil.sourceToTarget(dto, SysOperateLogDO.class);
        final UserDetail userDetail = sysUserService.getUserDetail(logDO.getCreator());
        logDO.setDeptId(userDetail.getDeptId());
        return sysOperateLogService.save(logDO);
    }

    @Override
    @DataSource("master")
    public Boolean insertLoginLog(LoginLogDTO dto) {
        SysLoginLogDO logDO = ConvertUtil.sourceToTarget(dto, SysLoginLogDO.class);
        return sysLoginLogService.save(logDO);
    }

    @Override
    @DataSource("master")
    @DataFilter(tableAlias = "boot_sys_operate_log")
    public IPage<SysOperateLogVO> queryOperateLogPage(SysOperateLogQO qo) {
        IPage<SysOperateLogVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysOperateLogService.getOperateLogList(page,qo);
    }

    @Override
    @DataSource("master")
    public IPage<SysLoginLogVO> queryLoginLogPage(LoginLogQO qo) {
        IPage<SysLoginLogVO> page = new Page<>(qo.getPageNum(),qo.getPageSize());
        return sysLoginLogService.getLoginLogList(page,qo);
    }

}
