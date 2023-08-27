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
import org.laokou.admin.client.dto.clientobject.OptionCO;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantGatewayImpl implements TenantGateway {

    private final TenantMapper tenantMapper;

    @Override
    public List<OptionCO> getOptionList() {
        List<TenantDO> list = tenantMapper.getOptionList();
        if (CollectionUtil.isNotEmpty(list)) {
            List<OptionCO> options = new ArrayList<>(list.size());
            for (TenantDO tenantDO : list) {
                OptionCO co = new OptionCO();
                co.setLabel(tenantDO.getName());
                co.setValue(String.valueOf(tenantDO.getId()));
                options.add(co);
            }
            return options;
        }
        return new ArrayList<>(0);
    }
}
