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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.OssGateway;
import org.laokou.admin.domain.oss.Oss;
import org.laokou.admin.gatewayimpl.database.OssMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.OssDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class OssGatewayImpl implements OssGateway {

    private final OssMapper ossMapper;

    @Override
    public Datas<Oss> list(Oss oss, PageQuery pageQuery) {
        IPage<OssDO> page = new Page<>(pageQuery.getPageNum(),pageQuery.getPageSize());
        IPage<OssDO> newPage = ossMapper.getOssListByLikeName(page, oss.getName(), pageQuery.getSqlFilter());
        Datas<Oss> datas = new Datas<>();
        datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(),Oss.class));
        datas.setTotal(newPage.getTotal());
        return datas;
    }

}
