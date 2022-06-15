package io.laokou.admin.domain.sys.repository.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysMenuDO;
import io.laokou.admin.domain.sys.repository.dao.SysMenuDao;
import io.laokou.admin.interfaces.qo.MenuQO;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.common.enums.SuperAdminEnum;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.RedisKeyUtil;
import io.laokou.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuDO> implements SysMenuService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<MenuVO> getMenuList(Long userId,Integer type) {
        List<MenuVO> menuList;
        if (userId == null) {
            menuList = this.baseMapper.getMenuList(type);
        } else {
            menuList = this.baseMapper.getMenuListByUserId(userId,type);
        }
        return menuList;
    }

    @Override
    public List<String> getPermissionsList() {
        return this.baseMapper.getPermissionsList();
    }

    @Override
    public List<String> getPermissionsListByUserId(Long userId) {
        return this.baseMapper.getPermissionsListByUserId(userId);
    }

    @Override
    public List<MenuVO> getMenuList(UserDetail userDetail, boolean noCache, Integer type) {
        //region Description
        if (noCache) {
            return getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
        }
        String userResourceKey = RedisKeyUtil.getUserResourceKey(userDetail.getId());
        String json = redisUtil.get(userResourceKey);
        List<MenuVO> resourceList;
        if (StringUtils.isNotBlank(json)) {
            resourceList = JSONObject.parseArray(json, MenuVO.class);
        } else {
            resourceList = getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
            redisUtil.set(userResourceKey, JSON.toJSONString(resourceList));
        }
        return resourceList;
        //endregion
    }

    private List<MenuVO> getMenuList(Long userId, Integer superAdmin,Integer type) {
        //region Description
        if (SuperAdminEnum.YES.value().equals(superAdmin)) {
            return getMenuList(null,type);
        } else {
            return getMenuList(userId,type);
        }
        //endregion
    }

    @Override
    public List<MenuVO> queryMenuList(MenuQO qo) {
        return this.baseMapper.queryMenuList(qo);
    }

    @Override
    public MenuVO getMenuById(Long id) {
        return this.baseMapper.getMenuById(id);
    }

    @Override
    public void deleteMenu(Long id) {
        this.baseMapper.deleteMenu(id);
    }

    @Override
    public List<MenuVO> getMenuListByRoleId(Long roleId) {
        return this.baseMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.getMenuIdsByRoleId(roleId);
    }

}
