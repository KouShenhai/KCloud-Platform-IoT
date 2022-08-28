package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysResourceDO;
import io.laokou.admin.infrastructure.common.index.ResourceIndex;
import io.laokou.admin.interfaces.qo.SysResourceQO;
import io.laokou.admin.interfaces.vo.SysResourceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/19 0019 下午 4:11
 */
@Mapper
@Repository
public interface SysResourceMapper extends BaseMapper<SysResourceDO> {

    IPage<SysResourceVO> getResourceList(IPage<SysResourceVO> page, @Param("qo") SysResourceQO qo);

    SysResourceVO getResourceById(@Param("id") Long id);

    void deleteResource(@Param("id") Long id);

    Long getResourceTotal(@Param("code")String code);

    List<String> getResourceYMPartitionList(@Param("code")String code);

    List<ResourceIndex> getResourceIndexList(@Param("pageSize")Integer pageSize, @Param("pageIndex")Integer pageIndex,@Param("code")String code);

}
