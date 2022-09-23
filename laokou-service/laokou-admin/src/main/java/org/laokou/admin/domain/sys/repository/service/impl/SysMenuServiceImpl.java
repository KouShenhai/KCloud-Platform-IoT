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
package org.laokou.admin.domain.sys.repository.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.domain.sys.entity.SysMenuDO;
import org.laokou.admin.domain.sys.repository.mapper.SysMenuMapper;
import org.laokou.admin.interfaces.qo.SysMenuQO;
import org.laokou.admin.interfaces.vo.SysMenuVO;
import org.laokou.admin.domain.sys.repository.service.SysMenuService;
import org.laokou.common.enums.SuperAdminEnum;
import org.laokou.common.user.UserDetail;
import org.laokou.common.utils.RedisKeyUtil;
import org.laokou.redis.RedisUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<SysMenuVO> getMenuList(Long userId, Integer type) {
        List<SysMenuVO> menuList;
        if (userId == null) {
            menuList = this.baseMapper.getMenuList(type);
        } else {
            menuList = this.baseMapper.getMenuListByUserId(userId,type);
        }
        return menuList;
    }

    @Override
    public List<String> getPermissionsList() {
        return this.baseMapper.getPermissionsList();
    }

    @Override
    public List<String> getPermissionsListByUserId(Long userId) {
        return this.baseMapper.getPermissionsListByUserId(userId);
    }

    @Override
    public List<SysMenuVO> getMenuList(UserDetail userDetail, boolean noCache, Integer type) {
        //region Description
        if (noCache) {
            return getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
        }
        String userResourceKey = RedisKeyUtil.getUserResourceKey(userDetail.getId());
        final RBucket<Object> bucket = redissonClient.getBucket(userResourceKey);
        List<SysMenuVO> resourceList;
        if (redisUtil.hasKey(userResourceKey)) {
            resourceList = (List<SysMenuVO>) bucket.get();
        } else {
            resourceList = getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
            bucket.set(resourceList,RedisUtil.HOUR_ONE_EXPIRE, TimeUnit.SECONDS);
        }
        return resourceList;
        //endregion
    }

    private List<SysMenuVO> getMenuList(Long userId, Integer superAdmin, Integer type) {
        //region Description
        if (SuperAdminEnum.YES.ordinal() == superAdmin) {
            return getMenuList(null,type);
        } else {
            return getMenuList(userId,type);
        }
        //endregion
    }

    @Override
    public List<SysMenuVO> queryMenuList(SysMenuQO qo) {
        return this.baseMapper.queryMenuList(qo);
    }

    @Override
    public SysMenuVO getMenuById(Long id) {
        return this.baseMapper.getMenuById(id);
    }

    @Override
    public void deleteMenu(Long id) {
        this.baseMapper.deleteMenu(id);
    }

    @Override
    public List<SysMenuVO> getMenuListByRoleId(Long roleId) {
        return this.baseMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

}
