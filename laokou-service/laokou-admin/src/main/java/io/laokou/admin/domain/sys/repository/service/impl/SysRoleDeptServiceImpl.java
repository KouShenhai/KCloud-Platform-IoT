package io.laokou.admin.domain.sys.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysRoleDeptDO;
import io.laokou.admin.domain.sys.repository.mapper.SysRoleDeptMapper;
import io.laokou.admin.domain.sys.repository.service.SysRoleDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/27 0027 上午 9:22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptMapper, SysRoleDeptDO> implements SysRoleDeptService {

}
