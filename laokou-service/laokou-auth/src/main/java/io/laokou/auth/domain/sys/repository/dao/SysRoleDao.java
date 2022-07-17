package io.laokou.auth.domain.sys.repository.dao;
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
public interface SysRoleDao {

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

    SysRoleVO getRoleById(@Param("id") Long id);

}
