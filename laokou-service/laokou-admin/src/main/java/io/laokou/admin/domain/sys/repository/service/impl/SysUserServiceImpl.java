package io.laokou.admin.domain.sys.repository.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.infrastructure.common.password.PasswordUtil;
import io.laokou.admin.interfaces.dto.SysUserDTO;
import io.laokou.admin.interfaces.qo.SysUserQO;
import io.laokou.admin.interfaces.vo.OptionVO;
import io.laokou.admin.interfaces.vo.SysUserVO;
import io.laokou.common.user.UserDetail;
import io.laokou.admin.domain.sys.entity.SysUserDO;
import io.laokou.admin.domain.sys.repository.mapper.SysUserMapper;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.common.utils.RedisKeyUtil;
import io.laokou.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author Kou Shenhai
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDO> implements SysUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void updateUser(SysUserDTO dto) {
        String password = dto.getPassword();
        if (StringUtils.isNotBlank(password)) {
            dto.setPassword(PasswordUtil.encode(password));
        }
        this.baseMapper.updateUser(dto);
        //删除用户缓存数据
        redisUtil.delete(RedisKeyUtil.getUserInfoKey(dto.getId()));
    }

    @Override
    public UserDetail getUserDetail(Long userId) {
        //region Description
        String userInfoKey = RedisKeyUtil.getUserInfoKey(userId);
        String json = redisUtil.get(userInfoKey);
        UserDetail userDetail;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(json)) {
            userDetail = JSON.parseObject(json, UserDetail.class);
        } else {
            userDetail = this.baseMapper.getUserDetail(userId,null);
        }
        return userDetail;
        //endregion
    }

    @Override
    public String getUsernameByOpenid(String zfbOpenid) {
        return this.baseMapper.getUsernameByOpenid(zfbOpenid);
    }

    @Override
    public IPage<SysUserVO> getUserPage(IPage<SysUserVO> page, SysUserQO qo) {
        return this.baseMapper.getUserPage(page, qo);
    }

    @Override
    public void deleteUser(Long id) {
        this.baseMapper.deleteUser(id);
    }

    @Override
    public List<SysUserVO> getUserList() {
        return this.baseMapper.getUserList();
    }

    @Override
    public List<SysUserVO> getUserListByUserId(Long id) {
        return this.baseMapper.getUserListByUserId(id);
    }

    @Override
    public List<OptionVO> getOptionList() {
        return this.baseMapper.getOptionList();
    }

}
