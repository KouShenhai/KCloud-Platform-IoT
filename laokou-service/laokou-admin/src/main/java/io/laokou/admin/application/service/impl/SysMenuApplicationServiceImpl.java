package io.laokou.admin.application.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.application.service.SysMenuApplicationService;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.MenuDTO;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.exception.CustomException;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuApplicationServiceImpl implements SysMenuApplicationService {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysAuthApplicationService sysAuthApplicationService;

    @Override
    public MenuVO getMenuList(HttpServletRequest request) {
        Long userId = Long.valueOf(request.getHeader(Constant.USER_KEY_HEAD));
        UserDetail userDetail = sysAuthApplicationService.getUserDetail(userId);
        List<MenuVO> menuList = sysMenuService.getMenuList(userDetail, true,0);
        return buildMenu(menuList);
    }

    @Override
    public List<MenuVO> queryMenuList(MenuQO qo) {
        return sysMenuService.queryMenuList(qo);
    }

    @Override
    public MenuVO getMenuById(Long id) {
        return sysMenuService.getMenuById(id);
    }

    @Override
    public Boolean updateMenu(MenuDTO dto,HttpServletRequest request) {
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        int count = sysMenuService.count(new LambdaQueryWrapper<SysMenuDO>().eq(SysMenuDO::getName, menuDO.getName()).ne(SysMenuDO::getId,menuDO.getId()));
        if (count > 0) {
            throw new CustomException("菜单名已存在，请重新输入");
        }
        menuDO.setEditor(SecurityUser.getUserId(request));
        return sysMenuService.updateById(menuDO);
    }

    @Override
    public Boolean insertMenu(MenuDTO dto,HttpServletRequest request) {
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        int count = sysMenuService.count(new LambdaQueryWrapper<SysMenuDO>().eq(SysMenuDO::getName, menuDO.getName()));
        if (count > 0) {
            throw new CustomException("菜单名已存在，请重新输入");
        }
        menuDO.setCreator(SecurityUser.getUserId(request));
        return sysMenuService.save(menuDO);
    }

    @Override
    public Boolean deleteMenu(Long id) {
        sysMenuService.deleteMenu(id);
        return true;
    }

    @Override
    public MenuVO treeMenu(Long roleId) {
        List<MenuVO> menuList;
        if (null == roleId) {
            menuList = queryMenuList(new MenuQO());
        } else {
            menuList = sysMenuService.getMenuListByRoleId(roleId);
        }
        return buildMenu(menuList);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return sysMenuService.getMenuIdsByRoleId(roleId);
    }

    /**
     * 组装树菜单
     * @param menuList
     * @return
     */
    private MenuVO buildMenu(List<MenuVO> menuList) {
        TreeUtil.TreeNo<TreeUtil.TreeNo> rootNode = TreeUtil.rootRootNode();
        MenuVO rootMenuNode = ConvertUtil.sourceToTarget(rootNode, MenuVO.class);
        return TreeUtil.buildTreeNode(menuList,rootMenuNode);
    }
}
