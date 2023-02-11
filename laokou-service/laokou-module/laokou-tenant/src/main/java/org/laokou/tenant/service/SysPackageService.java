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
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;

import java.util.List;

/**
 * @author laokou
 */
public interface SysPackageService extends IService<SysPackageDO> {

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 删除套餐
     * @param id
     * @return
     */
    Boolean deletePackage(Long id);

    /**
     * 查询套餐
     * @param qo
     * @param page
     * @return
     */
    IPage<SysPackageVO> queryPackagePage(IPage<SysPackageVO> page,SysPackageQo qo);

    /**
     * 查询详情
     * @param id
     * @return
     */
    SysPackageVO getPackageById(Long id);

    /**
     * 获取下拉框
     * @return
     */
    List<OptionVO> getOptionList();
}
