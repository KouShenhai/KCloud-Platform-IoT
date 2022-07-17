package io.laokou.auth.domain.sys.repository.service;

import io.laokou.auth.interfaces.vo.SysMenuVO;
import io.laokou.common.user.UserDetail;

import java.util.List;

/**
 * 菜单类
 * @author Kou Shenhai
 */
public interface SysMenuService {

    /**
     * 获取所有资源列表
     * @param userId
     * @return
     */
    List<SysMenuVO> getMenuList(Long userId, Integer type);

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
    List<String> getPermissionsListByUserId(Long userId);

    /**
     * 获取菜单列表
     * @param userDetail
     * @param noCache
     * @param type
     * @return
     */
    List<SysMenuVO> getMenuList(UserDetail userDetail, boolean noCache, Integer type);

}
