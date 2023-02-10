package org.laokou.admin.server.domain.sys.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.server.domain.sys.entity.SysOssDO;
import org.laokou.admin.server.interfaces.qo.SysOssQo;
import org.laokou.oss.client.vo.SysOssVO;
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

    /**
     * 分页查询
     * @param page
     * @param qo
     * @return
     */
    IPage<SysOssVO> queryOssPage(IPage<SysOssVO> page,@Param("qo") SysOssQo qo);

    /**
     * 查询详情
     * @param id
     * @return
     */
    SysOssVO getOssById(@Param("id")Long id);

}
