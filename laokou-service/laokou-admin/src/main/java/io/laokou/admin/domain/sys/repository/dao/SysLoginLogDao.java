package io.laokou.admin.domain.sys.repository.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.domain.sys.entity.SysLoginLogDO;
import io.laokou.admin.interfaces.qo.LoginLogQO;
import io.laokou.admin.interfaces.vo.SysLoginLogVO;
import io.laokou.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SysLoginLogDao extends BaseDao<SysLoginLogDO> {

    IPage<SysLoginLogVO> loginLogPage(IPage<SysLoginLogVO> page, @Param("qo") LoginLogQO qo);

}
