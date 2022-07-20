package io.laokou.admin.domain.sys.repository.service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.OptionVO;
import io.laokou.common.vo.SysUserVO;
import io.laokou.common.user.UserDetail;
import io.laokou.admin.domain.sys.entity.SysUserDO;

import java.util.List;

/**
 * 用户类
 * @author Kou Shenhai
 */
public interface SysUserService extends IService<SysUserDO> {

    /**
     * 修改用户信息
     * @param
     */
    void updateUser(SysUserDTO dto);

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

    /**
     * 分页查询用户
     * @param page
     * @param qo
     * @return
     */
    IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, SysUserQO qo);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 获取用户列表
     * @return
     */
    List<SysUserVO> getUserList();

    /**
     * 根据id获取用户列表
     * @param id
     * @return
     */
    List<SysUserVO> getUserListByUserId(Long id);

    /**
     * 获取用户列表下拉框
     * @return
     */
    List<OptionVO> getOptionList();
}
