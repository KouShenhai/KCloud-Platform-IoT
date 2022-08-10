package io.laokou.oauth2.mapper;
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
public interface SysMenuMapper {

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

}
