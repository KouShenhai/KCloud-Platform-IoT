package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysDeptDO;
import io.laokou.admin.interfaces.qo.SysDeptQO;
import io.laokou.common.vo.SysDeptVO;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/7/26 0026 下午 4:12
 */
public interface SysDeptService extends IService<SysDeptDO> {

    List<SysDeptVO> getDeptList(SysDeptQO qo);

    void deleteDept(Long id);

    SysDeptVO getDept(Long id);

}
