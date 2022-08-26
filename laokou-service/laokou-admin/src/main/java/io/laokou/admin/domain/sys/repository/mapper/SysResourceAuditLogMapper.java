package io.laokou.admin.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysResourceAuditLogDO;
import io.laokou.admin.interfaces.qo.SysResourceAuditLogQO;
import io.laokou.admin.interfaces.vo.SysResourceAuditLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/26 0026 下午 5:33
 */
@Mapper
@Repository
public interface SysResourceAuditLogMapper extends BaseMapper<SysResourceAuditLogDO> {

    IPage<SysResourceAuditLogVO> getAuditLogList(IPage<SysResourceAuditLogVO> page,@Param("qo") SysResourceAuditLogQO qo);

}
