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

package org.laokou.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.tenant.entity.SysTenantDO;
import org.laokou.tenant.qo.SysTenantQo;
import org.laokou.tenant.vo.SysTenantVO;

import java.util.List;

/**
 * @author laokou
 */
public interface SysTenantService extends IService<SysTenantDO> {
    /**
     * 下拉选择框
     * @return
     */
    List<OptionVO> getOptionList();

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 分页查询
     * @param page
     * @param qo
     * @return
     */
    IPage<SysTenantVO> queryTenantPage(IPage<SysTenantVO> page, SysTenantQo qo);


    /**
     * 查询租户
     * @param id
     * @return
     */
    SysTenantVO getTenantById(Long id);

    /**
     * 删除
     * @param id
     */
    void deleteTenant(Long id);
}
