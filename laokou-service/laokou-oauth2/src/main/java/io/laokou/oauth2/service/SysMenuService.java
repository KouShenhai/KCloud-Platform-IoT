package io.laokou.oauth2.service;

import java.util.List;

/**
 * 菜单类
 * @author Kou Shenhai
 */
public interface SysMenuService {
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

}
