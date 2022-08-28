package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysResourceDO;
import io.laokou.admin.infrastructure.common.index.ResourceIndex;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 4:11
 */
public interface SysResourceService extends IService<SysResourceDO> {
    IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, SysResourceQO qo);

    SysResourceVO getResourceById(Long id);

    void deleteResource(Long id);

    Long getResourceTotal(String code);

    List<String> getResourceYMPartitionList(String code);

    List<ResourceIndex> getResourceIndexList(Integer pageSize, Integer pageIndex,String code);
}
