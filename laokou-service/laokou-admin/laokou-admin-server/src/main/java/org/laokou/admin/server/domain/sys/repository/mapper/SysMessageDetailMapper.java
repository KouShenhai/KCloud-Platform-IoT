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
package org.laokou.admin.server.domain.sys.repository.mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.server.domain.sys.entity.SysMessageDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.laokou.common.mybatisplus.mapper.BaseBatchMapper;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface SysMessageDetailMapper extends BaseBatchMapper<SysMessageDetailDO> {

    /**
     * 获取版本号
     * @param id 主键
     * @return 返回版本号
     */
    Integer getVersion(@Param("id")Long id);

}
