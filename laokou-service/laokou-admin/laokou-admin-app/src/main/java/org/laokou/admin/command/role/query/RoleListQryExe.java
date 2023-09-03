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

package org.laokou.admin.command.role.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.role.RoleListQry;
import org.laokou.admin.client.dto.role.clientobject.RoleCO;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleListQryExe {

    private final RoleMapper roleMapper;

    public Result<Datas<RoleCO>> execute(RoleListQry qry) {
        IPage<RoleDO> page = new Page<>(qry.getPageNum(),qry.getPageSize());
        IPage<RoleDO> newPage = roleMapper.getRoleListByTenantIdAndLikeName(page, UserUtil.getTenantId(), qry.getName());
        Datas<RoleCO> datas = new Datas<>();
        datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), RoleCO.class));
        datas.setTotal(newPage.getTotal());
        return Result.of(datas);
    }

}
