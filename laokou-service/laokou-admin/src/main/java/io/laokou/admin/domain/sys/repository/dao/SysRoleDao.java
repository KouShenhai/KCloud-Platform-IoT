package io.laokou.admin.domain.sys.repository.dao;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.interfaces.qo.RoleQO;
import io.laokou.admin.interfaces.vo.RoleVO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * 角色类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysRoleDao extends BaseDao<SysRoleDO> {

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
    List<Long> getRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据userId获取角色名称
     * @param userId
     * @return
     */
    List<String> getRoleNameList(@Param("userId")Long userId);

    /**
     * 分页查询角色
     * @param page
     * @param qo
     * @return
     */
    IPage<RoleVO> getRolePage(IPage<RoleVO> page, @Param("qo") RoleQO qo);

    RoleVO getRoleById(@Param("id") Long id);

    void deleteRole(@Param("id") Long id);

}
