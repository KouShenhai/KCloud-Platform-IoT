package io.laokou.auth.domain.sys.repository.service;
import io.laokou.common.vo.SysRoleVO;

import java.util.List;

public interface SysRoleService {

    /**
     * 查询角色Ids
     * @return
     */
    List<Long> getRoleIds();
    /**
     * 通过userId查询角色Ids
     * @param userId
     * @return
     */

    List<Long> getRoleIdsByUserId(Long userId);

    List<SysRoleVO> getRoleListByUserId(Long userId);

}
