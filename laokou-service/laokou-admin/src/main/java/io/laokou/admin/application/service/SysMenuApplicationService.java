package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.MenuDTO;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysMenuApplicationService {

    MenuVO getMenuList(HttpServletRequest request);

    List<MenuVO> queryMenuList(MenuQO dto);

    MenuVO getMenuById(Long id);

    Boolean updateMenu(MenuDTO dto,HttpServletRequest request);

    Boolean insertMenu(MenuDTO dto,HttpServletRequest request);

    Boolean deleteMenu(Long id);

    MenuVO treeMenu(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
