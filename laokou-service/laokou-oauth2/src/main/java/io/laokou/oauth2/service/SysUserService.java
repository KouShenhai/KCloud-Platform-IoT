package io.laokou.oauth2.service;
import io.laokou.common.user.BaseUserVO;
import io.laokou.common.user.UserDetail;

/**
 * 用户类
 * @author Kou Shenhai
 */
public interface SysUserService {

    /**
     * 获取用户信息
     * @param id
     * @param username
     * @return
     */
    UserDetail getUserDetail(Long id,String username);

    BaseUserVO getUserInfo(String username);
}
