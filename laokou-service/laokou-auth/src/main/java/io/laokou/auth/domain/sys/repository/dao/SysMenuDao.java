package io.laokou.auth.domain.sys.repository.dao;
import io.laokou.auth.interfaces.vo.SysMenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * 菜单类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysMenuDao {

    /**
     * 查询所有权限列表
     * @return
     */
    List<String> getPermissionsList();

    /**
     * 查询用户权限列表
     * @param userId
     * @return
     */
    List<String> getPermissionsListByUserId(@Param("userId") Long userId);

    /**
     * 获取所有的资源列表
     * @return
     */
    List<SysMenuVO> getMenuList(@Param("type")Integer type);

    /**
     * 通过userId查询资源权限
     * @param userId
     * @return
     */
    List<SysMenuVO> getMenuListByUserId(@Param("userId") Long userId, @Param("type")Integer type);

}
