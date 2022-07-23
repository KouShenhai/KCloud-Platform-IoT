package io.laokou.admin.domain.sys.repository.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysRoleDO;
import io.laokou.admin.interfaces.qo.SysRoleQO;
import io.laokou.common.vo.SysRoleVO;
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
public interface SysRoleMapper extends BaseMapper<SysRoleDO> {

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
    List<SysRoleVO> getRoleListByUserId(@Param("userId")Long userId);

    /**
     * 分页查询角色
     * @param page
     * @param qo
     * @return
     */
    IPage<SysRoleVO> getRoleList(IPage<SysRoleVO> page, @Param("qo") SysRoleQO qo);

    SysRoleVO getRoleById(@Param("id") Long id);

    void deleteRole(@Param("id") Long id);

    List<SysRoleVO> getRoleList(@Param("qo") SysRoleQO qo);

}
