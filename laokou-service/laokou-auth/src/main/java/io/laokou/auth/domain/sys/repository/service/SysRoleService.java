package io.laokou.auth.domain.sys.repository.service;
import io.laokou.common.vo.SysRoleVO;

import java.util.List;

public interface SysRoleService {

    List<SysRoleVO> getRoleListByUserId(Long userId);

}
