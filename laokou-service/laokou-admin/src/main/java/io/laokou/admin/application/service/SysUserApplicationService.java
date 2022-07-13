package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.SysUserVO;

import javax.servlet.http.HttpServletRequest;

public interface SysUserApplicationService {

    /**
     * 修改用户信息
     * @param
     */
    Boolean updateUser(SysUserDTO dto, HttpServletRequest request);

    Boolean insertUser(SysUserDTO dto, HttpServletRequest request);

    IPage<SysUserVO> queryUserPage(SysUserQO qo);

    SysUserVO getUserById(Long id);

    Boolean deleteUser(Long id,HttpServletRequest request);

}
