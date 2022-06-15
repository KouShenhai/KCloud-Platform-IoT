package io.laokou.admin.domain.sys.repository.dao;

import io.laokou.admin.interfaces.dto.MenuDTO;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.common.dao.BaseDao;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
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
public interface SysMenuDao extends BaseDao<SysMenuDO> {

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
    List<MenuVO> getMenuList(@Param("type")Integer type);

    /**
     * 通过userId查询资源权限
     * @param userId
     * @return
     */
    List<MenuVO> getMenuListByUserId(@Param("userId") Long userId,@Param("type")Integer type);

    /**
     * 查询菜单列表
     * @param qo
     * @return
     */
    List<MenuVO> queryMenuList(@Param("qo") MenuQO qo);

    /**
     * 根据id查询菜单
     * @param id
     * @return
     */
    MenuVO getMenuById(@Param("id") Long id);

    /**
     * 逻辑删除
     * @param id
     */
    void deleteMenu(@Param("id") Long id);

    /**
     * 通过roleId获取菜单
     * @param roleId
     * @return
     */
    List<MenuVO> getMenuListByRoleId(@Param("roleId") Long roleId);

    List<Long> getMenuIdsByRoleId(@Param("roleId") Long roleId);

}
