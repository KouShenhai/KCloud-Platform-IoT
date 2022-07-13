package io.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
import io.laokou.admin.interfaces.qo.SysMenuQO;
import io.laokou.admin.interfaces.vo.SysMenuVO;
import io.laokou.common.user.UserDetail;
import java.util.List;
/**
 * 菜单类
 * @author Kou Shenhai
 */
public interface SysMenuService extends IService<SysMenuDO> {

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

    /**
     * 查询列表
     * @param qo
     * @return
     */
    List<SysMenuVO> queryMenuList(SysMenuQO qo);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    SysMenuVO getMenuById(Long id);

    /**
     * 逻辑删除
     */
    void deleteMenu(Long id);

    /**
     * 通过roleId获取菜单
     * @param roleId
     * @return
     */
    List<SysMenuVO> getMenuListByRoleId(Long roleId);

    List<Long> getMenuIdsByRoleId(Long roleId);

}
