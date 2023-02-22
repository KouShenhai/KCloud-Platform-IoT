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
package org.laokou.auth.server.domain.sys.repository.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.auth.client.user.UserDetail;
import org.springframework.stereotype.Repository;
/**
 * 用户类
 * @author laokou
 */
@Mapper
@Repository
public interface SysUserMapper {

    /**
     * 获取用户信息
     * @param loginName
     * @param tenantId
     * @param loginType
     * @return
     */
    UserDetail getUserDetail(@Param("loginName")String loginName,@Param("tenantId")Long tenantId,@Param("loginType")String loginType);


}
