package io.laokou.auth.domain.sys.repository.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.laokou.auth.domain.sys.repository.dao.SysMenuDao;
import io.laokou.auth.domain.sys.repository.service.SysMenuService;
import io.laokou.auth.interfaces.vo.SysMenuVO;
import io.laokou.common.enums.SuperAdminEnum;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.RedisKeyUtil;
import io.laokou.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Kou Shenhai
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysMenuServiceImpl implements SysMenuService {

    private final RedisUtil redisUtil;

    private final SysMenuDao sysMenuDao;

    @Override
    public List<SysMenuVO> getMenuList(Long userId, Integer type) {
        List<SysMenuVO> menuList;
        if (userId == null) {
            menuList = sysMenuDao.getMenuList(type);
        } else {
            menuList = sysMenuDao.getMenuListByUserId(userId,type);
        }
        return menuList;
    }

    @Override
    public List<String> getPermissionsList() {
        return sysMenuDao.getPermissionsList();
    }

    @Override
    public List<String> getPermissionsListByUserId(Long userId) {
        return sysMenuDao.getPermissionsListByUserId(userId);
    }

    @Override
    public List<SysMenuVO> getMenuList(UserDetail userDetail, boolean noCache, Integer type) {
        //region Description
        if (noCache) {
            return getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
        }
        String userResourceKey = RedisKeyUtil.getUserResourceKey(userDetail.getId());
        String json = redisUtil.get(userResourceKey);
        List<SysMenuVO> resourceList;
        if (StringUtils.isNotBlank(json)) {
            resourceList = JSONObject.parseArray(json, SysMenuVO.class);
        } else {
            resourceList = getMenuList(userDetail.getId(),userDetail.getSuperAdmin(),type);
            redisUtil.set(userResourceKey, JSON.toJSONString(resourceList),RedisUtil.HOUR_ONE_EXPIRE);
        }
        return resourceList;
        //endregion
    }

    private List<SysMenuVO> getMenuList(Long userId, Integer superAdmin, Integer type) {
        //region Description
        if (SuperAdminEnum.YES.ordinal() == superAdmin) {
            return getMenuList(null,type);
        } else {
            return getMenuList(userId,type);
        }
        //endregion
    }
}
