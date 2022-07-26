package io.laokou.auth.domain.sys.repository.service;
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

    /**
     * 根据openid获取username
     * @param zfbOpenid 支付宝唯一用户标识
     * @return
     */
    String getUsernameByOpenid(String zfbOpenid);

}
