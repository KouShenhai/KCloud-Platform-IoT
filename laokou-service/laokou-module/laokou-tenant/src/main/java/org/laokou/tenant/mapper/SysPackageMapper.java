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
package org.laokou.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.tenant.entity.SysPackageDO;
import org.laokou.tenant.qo.SysPackageQo;
import org.laokou.tenant.vo.SysPackageVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SysPackageMapper extends BaseMapper<SysPackageDO> {

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 获取详情
     * @param id
     * @return
     */
    SysPackageVO getPackageById(Long id);

    /**
     * 查询套餐
     * @param page
     * @param qo
     * @return
     */
    IPage<SysPackageVO> queryPackagePage(IPage<SysPackageVO> page,@Param("qo") SysPackageQo qo);

    /**
     * 获取下拉框
     * @return
     */
    List<OptionVO> getOptionList();
}
