package io.laokou.admin.domain.sys.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysOperateLogDO;
import io.laokou.admin.interfaces.qo.SysOperateLogQO;
import io.laokou.admin.interfaces.vo.SysOperateLogVO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysOperateLogDao extends BaseDao<SysOperateLogDO> {

    IPage<SysOperateLogVO> operateLogPage(IPage<SysOperateLogVO> page, @Param("qo") SysOperateLogQO qo);

}
