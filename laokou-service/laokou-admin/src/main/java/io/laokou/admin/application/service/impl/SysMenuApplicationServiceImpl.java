/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.laokou.admin.application.service.SysMenuApplicationService;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.SysMenuDTO;
import io.laokou.admin.interfaces.qo.SysMenuQO;
import io.laokou.admin.interfaces.vo.SysMenuVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.TreeUtil;
import io.laokou.datasource.annotation.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
public class SysMenuApplicationServiceImpl implements SysMenuApplicationService {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    @DataSource("master")
    public SysMenuVO getMenuList(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader(Constant.USER_KEY_HEAD));
        UserDetail userDetail = sysUserService.getUserDetail(userId);
        List<SysMenuVO> menuList = sysMenuService.getMenuList(userDetail, true,0);
        return buildMenu(menuList);
    }

    @Override
    @DataSource("master")
    public List<SysMenuVO> queryMenuList(SysMenuQO qo) {
        return sysMenuService.queryMenuList(qo);
    }

    @Override
    @DataSource("master")
    public SysMenuVO getMenuById(Long id) {
        return sysMenuService.getMenuById(id);
    }

    @Override
    @DataSource("master")
    public Boolean updateMenu(SysMenuDTO dto, HttpServletRequest request) {
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        int count = sysMenuService.count(Wrappers.lambdaQuery(SysMenuDO.class).eq(SysMenuDO::getName, menuDO.getName()).eq(SysMenuDO::getDelFlag, Constant.NO).ne(SysMenuDO::getId,menuDO.getId()));
        if (count > 0) {
            throw new CustomException("菜单已存在，请重新填写");
        }
        menuDO.setEditor(SecurityUser.getUserId(request));
        return sysMenuService.updateById(menuDO);
    }

    @Override
    @DataSource("master")
    public Boolean insertMenu(SysMenuDTO dto, HttpServletRequest request) {
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        int count = sysMenuService.count(Wrappers.lambdaQuery(SysMenuDO.class).eq(SysMenuDO::getName, menuDO.getName()).eq(SysMenuDO::getDelFlag, Constant.NO));
        if (count > 0) {
            throw new CustomException("菜单已存在，请重新填写");
        }
        menuDO.setCreator(SecurityUser.getUserId(request));
        return sysMenuService.save(menuDO);
    }

    @Override
    @DataSource("master")
    public Boolean deleteMenu(Long id) {
        sysMenuService.deleteMenu(id);
        return true;
    }

    @Override
    @DataSource("master")
    public SysMenuVO treeMenu(Long roleId) {
        List<SysMenuVO> menuList;
        if (null == roleId) {
            menuList = queryMenuList(new SysMenuQO());
        } else {
            menuList = sysMenuService.getMenuListByRoleId(roleId);
        }
        return buildMenu(menuList);
    }

    @Override
    @DataSource("master")
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return sysMenuService.getMenuIdsByRoleId(roleId);
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
