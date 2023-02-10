package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.server.domain.sys.entity.SysOssDO;
import org.springframework.stereotype.Repository;

/**
 * @author laokou
 */
@Repository
@Mapper
public interface SysOssMapper extends BaseMapper<SysOssDO> {

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(@Param("id") Long id);

}
