package io.laokou.admin.application.service.impl;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.application.service.SysMenuApplicationService;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.admin.infrastructure.common.user.SecurityUser;
import io.laokou.admin.interfaces.dto.MenuDTO;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.ConvertUtil;
import io.laokou.common.utils.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Service
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
        menuDO.setEditor(SecurityUser.getUserId(request));
        return sysMenuService.updateById(menuDO);
    }

    @Override
    public Boolean insertMenu(MenuDTO dto,HttpServletRequest request) {
        SysMenuDO menuDO = ConvertUtil.sourceToTarget(dto, SysMenuDO.class);
        menuDO.setCreator(SecurityUser.getUserId(request));
        return sysMenuService.save(menuDO);
    }

    @Override
    public Boolean deleteMenu(Long id) {
        sysMenuService.deleteMenu(id);
        return Boolean.TRUE;
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
