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
package io.laokou.auth.domain.sys.repository.mapper;
import io.laokou.common.user.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 用户类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysUserMapper {

    /**
     * 获取用户信息
     * @param userId
     * @param username
     * @return
     */
    UserDetail getUserDetail(@Param("userId")Long userId,@Param("username")String username);

    /**
     * 根据openid获取username
     * @param zfbOpenid 支付宝用户唯一标识
     * @return
     */
    String getUsernameByOpenid(@Param("zfbOpenid")String zfbOpenid);

    void updateZfbOpenid(@Param("id")Long id, @Param("zfbOpenid")String zfbOpenid);

}
