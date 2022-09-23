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
package org.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.laokou.admin.domain.sys.entity.SysDeptDO;
import org.laokou.admin.interfaces.qo.SysDeptQO;
import org.laokou.common.vo.SysDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:15
 */
@Mapper
@Repository
public interface SysDeptMapper extends BaseMapper<SysDeptDO> {

    List<SysDeptVO> getDeptList(@Param("qo") SysDeptQO qo);

    void deleteDept(@Param("id") Long id);

    SysDeptVO getDept(@Param("id") Long id);

    List<Long> getDeptIdsByRoleId(@Param("roleId") Long roleId);

}
