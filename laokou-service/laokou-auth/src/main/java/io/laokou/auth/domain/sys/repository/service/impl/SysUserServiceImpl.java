package io.laokou.auth.domain.sys.repository.service.impl;
import io.laokou.auth.domain.sys.repository.mapper.SysUserMapper;
import io.laokou.auth.domain.sys.repository.service.SysUserService;
import io.laokou.common.user.UserDetail;
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

    @Override
    public UserDetail getUserDetail(Long id, String username) {
        return sysUserMapper.getUserDetail(id, username);
    }

    @Override
    public String getUsernameByOpenid(String zfbOpenid) {
        return sysUserMapper.getUsernameByOpenid(zfbOpenid);
    }

    @Override
    public void updateZfbOpenid(Long id, String zfbOpenid) {
        sysUserMapper.updateZfbOpenid(id,zfbOpenid);
    }


}
