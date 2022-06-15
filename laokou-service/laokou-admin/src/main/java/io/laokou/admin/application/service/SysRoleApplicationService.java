package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.RoleDTO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysRoleApplicationService {

    IPage<RoleVO> getRolePage(RoleQO qo);

    List<RoleVO> getRoleList(RoleQO qo);

    RoleVO getRoleById(Long id);

    Boolean insertRole(RoleDTO dto, HttpServletRequest request);

    Boolean updateRole(RoleDTO dto, HttpServletRequest request);

    Boolean deleteRole(Long id);
}
