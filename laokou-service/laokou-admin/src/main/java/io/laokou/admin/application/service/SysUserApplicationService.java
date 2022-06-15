package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.UserDTO;
import io.laokou.admin.interfaces.qo.UserQO;
import io.laokou.admin.interfaces.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

public interface SysUserApplicationService {

    /**
     * 修改用户信息
     * @param
     */
    Boolean updateUser(UserDTO dto, HttpServletRequest request);

    Boolean insertUser(UserDTO dto, HttpServletRequest request);

    IPage<UserVO> getUserPage(UserQO qo);

    UserVO getUserById(Long id);

    Boolean deleteUser(Long id);

}
