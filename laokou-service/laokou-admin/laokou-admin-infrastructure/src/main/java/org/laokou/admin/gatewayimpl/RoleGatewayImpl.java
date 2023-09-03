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

package org.laokou.admin.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.common.Option;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

    private final RoleMapper roleMapper;

    @Override
    public Boolean insert() {
        return null;
    }

    @Override
    public Boolean update() {
        return null;
    }

    @Override
    public List<Option> optionList() {
        List<RoleDO> list = roleMapper.getValueListOrderByDesc(RoleDO.class, "create_date", "id", "name");
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<Option> options = new ArrayList<>(list.size());
        for (RoleDO roleDO : list) {
            Option o = new Option();
            o.setLabel(roleDO.getName());
            o.setValue(String.valueOf(roleDO.getId()));
            options.add(o);
        }
        return options;
    }
}
