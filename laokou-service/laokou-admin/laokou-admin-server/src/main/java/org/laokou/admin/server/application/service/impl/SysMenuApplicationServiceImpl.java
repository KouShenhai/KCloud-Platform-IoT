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
package org.laokou.admin.server.application.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.server.application.service.SysMenuApplicationService;
import org.laokou.admin.server.domain.sys.entity.SysMenuDO;
import org.laokou.admin.server.domain.sys.repository.service.SysMenuService;
import org.laokou.admin.server.interfaces.qo.SysMenuQo;
import org.laokou.admin.client.vo.SysMenuVO;
import org.laokou.admin.client.dto.SysMenuDTO;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.redis.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SysMenuApplicationServiceImpl implements SysMenuApplicationService {

    private final SysMenuService sysMenuService;

    private final RedisUtil redisUtil;

    @Override
    public SysMenuVO getMenuList() {
        UserDetail userDetail = UserUtil.userDetail();
        Long userId = userDetail.getUserId();
        String resourceTreeKey = RedisKeyUtil.getResourceTreeKey(userId);
        Object obj = redisUtil.get(resourceTreeKey);
        if (obj != null) {
            return (SysMenuVO) obj;
        }
        List<SysMenuVO> menuList = sysMenuService.getMenuList(userDetail,0);
        SysMenuVO sysMenuVO = buildMenu(menuList);
        redisUtil.set(resourceTreeKey,sysMenuVO,RedisUtil.HOUR_ONE_EXPIRE);
        return sysMenuVO;
    }

    @Override
    public List<SysMenuVO> queryMenuList(SysMenuQo qo) {
        return sysMenuService.queryMenuList(qo);
    }

    @Override
    public SysMenuVO getMenuById(Long id) {
        return sysMenuService.getMenuById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMenu(SysMenuDTO dto) {
        ValidatorUtil.validateEntity(dto);
        Long id = dto.getId();
        if (id == null) {
            throw new CustomException("菜单编号不为空");
        }
        long count = sysMenuService.count(Wrappers.lambdaQuery(SysMenuDO.class)
                .eq(SysMenuDO::getName, dto.getName()).ne(SysMenuDO::getId,dto.getId()));
        if (count > 0) {
            throw new CustomException("菜单已存在，请重新填写");
        }
        Integer version = sysMenuService.getVersion(id);
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        menuDO.setVersion(version);
        menuDO.setEditor(UserUtil.getUserId());
        return sysMenuService.updateById(menuDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertMenu(SysMenuDTO dto) {
        ValidatorUtil.validateEntity(dto);
        long count = sysMenuService.count(Wrappers.lambdaQuery(SysMenuDO.class)
                .eq(SysMenuDO::getName, dto.getName()));
        if (count > 0) {
            throw new CustomException("菜单已存在，请重新填写");
        }
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        menuDO.setCreator(UserUtil.getUserId());
        return sysMenuService.save(menuDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMenu(Long id) {
        sysMenuService.deleteMenu(id);
        return true;
    }

    @Override
    public SysMenuVO treeMenu() {
        List<SysMenuVO> menuList = queryMenuList(new SysMenuQo());
        return buildMenu(menuList);
    }

    @Override

    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return sysMenuService.getMenuIdsByRoleId(roleId);
    }

    @Override
    public SysMenuVO treeTenantMenu() {
        List<SysMenuVO> tenantMenuList = sysMenuService.getTenantMenuList();
        return buildMenu(tenantMenuList);
    }

    /**
     * 组装树菜单
     * @param menuList
     * @return
     */
    private SysMenuVO buildMenu(List<SysMenuVO> menuList) {
        TreeUtil.TreeNo<TreeUtil.TreeNo> rootNode = TreeUtil.rootRootNode();
        SysMenuVO rootMenuNode = ConvertUtil.sourceToTarget(rootNode, SysMenuVO.class);
        return TreeUtil.buildTreeNode(menuList,rootMenuNode);
    }
}
