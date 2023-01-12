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

import java.util.*;

import org.laokou.admin.client.vo.SysAuditLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:33
 */
@Mapper
@Repository
public interface SysAuditLogMapper {

    /**
     * 查询资源审核日志
     * @param businessId
     * @param type
     * @return
     */
    List<SysAuditLogVO> getAuditLogList(@Param("businessId") Long businessId,@Param("type")Integer type);

}
