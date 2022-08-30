package io.laokou.auth.domain.sys.repository.mapper;
import io.laokou.common.user.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 用户类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysUserMapper {

    /**
     * 获取用户信息
     * @param userId
     * @param username
     * @return
     */
    UserDetail getUserDetail(@Param("userId")Long userId,@Param("username")String username);

    /**
     * 根据openid获取username
     * @param zfbOpenid 支付宝用户唯一标识
     * @return
     */
    String getUsernameByOpenid(@Param("zfbOpenid")String zfbOpenid);

    void updateZfbOpenid(@Param("id")Long id, @Param("zfbOpenid")String zfbOpenid);

}
