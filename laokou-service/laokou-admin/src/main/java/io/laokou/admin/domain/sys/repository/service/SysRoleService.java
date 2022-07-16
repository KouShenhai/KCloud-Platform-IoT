package io.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.common.vo.SysRoleVO;

import java.util.List;
public interface SysRoleService extends IService<SysRoleDO> {

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

    /**
     * 分页查询角色
     * @param page
     * @param qo
     * @return
     */
    IPage<SysRoleVO> getRolePage(IPage<SysRoleVO> page, SysRoleQO qo);

    List<SysRoleVO> getRoleList(SysRoleQO qo);

    SysRoleVO getRoleById(Long id);

    void deleteRole(Long id);

}
