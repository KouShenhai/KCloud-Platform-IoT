package io.laokou.auth.domain.sys.repository.dao;
import io.laokou.common.user.UserDetail;
import io.laokou.common.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * 用户类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysUserDao{

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

    List<SysUserVO> getUserListByUserId(@Param("id") Long id);

    List<SysUserVO> getUserList();
}
