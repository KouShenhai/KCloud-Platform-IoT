package io.laokou.auth.domain.sys.repository.service.impl;
import io.laokou.auth.domain.sys.repository.dao.SysUserDao;
import io.laokou.auth.domain.sys.repository.service.SysUserService;
import io.laokou.common.user.UserDetail;
import io.laokou.common.vo.SysUserVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@Slf4j
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {

    private final SysUserDao sysUserDao;

    @Override
    public UserDetail getUserDetail(Long id, String username) {
        return sysUserDao.getUserDetail(id, username);
    }

    @Override
    public String getUsernameByOpenid(String zfbOpenid) {
        return sysUserDao.getUsernameByOpenid(zfbOpenid);
    }

    @Override
    public List<SysUserVO> getUserList() {
        return sysUserDao.getUserList();
    }

    @Override
    public List<SysUserVO> getUserListByUserId(Long id) {
        return sysUserDao.getUserListByUserId(id);
    }

}
