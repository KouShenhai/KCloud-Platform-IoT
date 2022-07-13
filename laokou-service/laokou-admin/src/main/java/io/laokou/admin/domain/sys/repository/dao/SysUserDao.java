package io.laokou.admin.domain.sys.repository.dao;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.SysUserVO;
import io.laokou.common.dao.BaseDao;
import io.laokou.common.user.UserDetail;
import io.laokou.admin.domain.sys.entity.SysUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/**
 * 用户类
 * @author  Kou Shenhai
 */
@Mapper
@Repository
public interface SysUserDao extends BaseDao<SysUserDO> {

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

    /**
     * 分页查询用户
     * @param page
     * @param qo
     * @return
     */
    IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, @Param("qo") SysUserQO qo);

    /**
     * 更新用户信息
     * @param dto
     */
    void updateUser(@Param("dto") SysUserDTO dto);

    void deleteUser(@Param("id") Long id);
}
