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
     * 根据userId获取角色名称
     * @param userId
     * @return
     */
    List<SysRoleVO> getRoleListByUserId(@Param("userId")Long userId);

}
