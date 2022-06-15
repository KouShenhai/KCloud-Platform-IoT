package io.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.interfaces.dto.UserDTO;
import io.laokou.admin.interfaces.qo.UserQO;
import io.laokou.admin.interfaces.vo.UserVO;
import io.laokou.common.user.UserDetail;
import io.laokou.admin.domain.sys.entity.SysUserDO;
/**
 * 用户类
 * @author Kou Shenhai
 */
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 修改用户信息
     * @param
     */
    void updateUser(UserDTO dto);

    /**
     * 获取用户信息
     * @param id
     * @param username
     * @return
     */
    UserDetail getUserDetail(Long id,String username);

    /**
     * 根据openid获取username
     * @param zfbOpenid
     * @param wxgzhOpenid
     * @return
     */
    String getUsernameByOpenid(String zfbOpenid,String wxgzhOpenid,String wxOpenid);

    /**
     * 分页查询用户
     * @param page
     * @param qo
     * @return
     */
    IPage<UserVO> getUserPage(IPage<UserVO> page,UserQO qo);

    void deleteUser(Long id);

}
