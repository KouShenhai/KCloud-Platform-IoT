package io.laokou.admin.application.service;

import io.laokou.admin.interfaces.dto.SysMenuDTO;
import io.laokou.admin.interfaces.qo.SysMenuQO;
import io.laokou.admin.interfaces.vo.SysMenuVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysMenuApplicationService {

    SysMenuVO getMenuList(HttpServletRequest request);

    List<SysMenuVO> queryMenuList(SysMenuQO dto);

    SysMenuVO getMenuById(Long id);

    Boolean updateMenu(SysMenuDTO dto, HttpServletRequest request);

    Boolean insertMenu(SysMenuDTO dto, HttpServletRequest request);

    Boolean deleteMenu(Long id);

    SysMenuVO treeMenu(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
