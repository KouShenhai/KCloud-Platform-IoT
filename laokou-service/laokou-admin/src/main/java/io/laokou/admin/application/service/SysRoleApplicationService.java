package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysRoleDTO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.admin.interfaces.vo.SysRoleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysRoleApplicationService {

    IPage<SysRoleVO> queryRolePage(SysRoleQO qo);

    List<SysRoleVO> getRoleList(SysRoleQO qo);

    SysRoleVO getRoleById(Long id);

    Boolean insertRole(SysRoleDTO dto, HttpServletRequest request);

    Boolean updateRole(SysRoleDTO dto, HttpServletRequest request);

    Boolean deleteRole(Long id);
}
