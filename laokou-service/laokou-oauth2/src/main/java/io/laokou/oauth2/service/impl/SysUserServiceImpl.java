package io.laokou.oauth2.service.impl;
import io.laokou.common.user.BaseUserVO;
import io.laokou.common.user.UserDetail;
import io.laokou.oauth2.mapper.SysUserMapper;
import io.laokou.oauth2.service.SysMenuService;
import io.laokou.oauth2.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kou Shenhai
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetail getUserDetail(Long id, String username) {
        return sysUserMapper.getUserDetail(id, username);
    }

    @Override
    public BaseUserVO getUserInfo(String username) {
        UserDetail userDetail = sysUserMapper.getUserDetail(null,username);
        return BaseUserVO.builder().imgUrl(userDetail.getImgUrl())
                .username(userDetail.getUsername())
                .userId(userDetail.getId())
                .mobile(userDetail.getMobile())
                .email(userDetail.getEmail()).build();
    }

}
