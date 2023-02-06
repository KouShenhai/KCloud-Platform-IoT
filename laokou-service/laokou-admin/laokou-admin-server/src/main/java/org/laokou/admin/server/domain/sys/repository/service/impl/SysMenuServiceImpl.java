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
package org.laokou.admin.server.domain.sys.repository.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.laokou.admin.server.domain.sys.entity.SysMenuDO;
import org.laokou.admin.server.domain.sys.repository.mapper.SysMenuMapper;
import org.laokou.admin.server.interfaces.qo.SysMenuQo;
import org.laokou.admin.client.vo.SysMenuVO;
import org.laokou.admin.server.domain.sys.repository.service.SysMenuService;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.enums.SuperAdminEnum;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @author laokou
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuDO> implements SysMenuService {

    @Override
    public List<SysMenuVO> getMenuList(Long userId, Integer type) {
        List<SysMenuVO> menuList;
        if (userId == null) {
            Long tenantId = UserUtil.getTenantId();
            menuList = this.baseMapper.getMenuList(type,tenantId);
        } else {
            menuList = this.baseMapper.getMenuListByUserId(userId,type);
        }
        return menuList;
    }

    @Override
    public List<SysMenuVO> getMenuList(UserDetail userDetail, Integer type) {
        //region Description
        return getMenuList(userDetail.getUserId(),userDetail.getSuperAdmin(),type);
        //endregion
    }

    private List<SysMenuVO> getMenuList(Long userId, Integer superAdmin, Integer type) {
        //region Description
        if (SuperAdminEnum.YES.ordinal() == superAdmin) {
            Long tenantId = UserUtil.getTenantId();
            return this.baseMapper.getMenuList(type,tenantId);
        } else {
            return this.baseMapper.getMenuListByUserId(userId,type);
        }
        //endregion
    }

    @Override
    public List<SysMenuVO> queryMenuList(SysMenuQo qo) {
        return this.baseMapper.queryMenuList(qo);
    }

    @Override
    public SysMenuVO getMenuById(Long id) {
        return this.baseMapper.getMenuById(id);
    }

    @Override
    public void deleteMenu(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenuVO> getMenuListByRoleId(Long roleId) {
        return this.baseMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

    @Override
    public Integer getVersion(Long id) {
        return this.baseMapper.getVersion(id);
    }

}
