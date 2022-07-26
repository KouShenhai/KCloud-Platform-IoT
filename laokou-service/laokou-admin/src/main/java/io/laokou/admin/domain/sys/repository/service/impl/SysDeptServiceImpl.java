package io.laokou.admin.domain.sys.repository.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.sys.entity.SysDeptDO;
import io.laokou.admin.domain.sys.repository.mapper.SysDeptMapper;
import io.laokou.admin.domain.sys.repository.service.SysDeptService;
import io.laokou.common.vo.SysDeptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptDO> implements SysDeptService {

    @Override
    public List<SysDeptVO> getDeptList() {
        return this.baseMapper.getDeptList();
    }
}
