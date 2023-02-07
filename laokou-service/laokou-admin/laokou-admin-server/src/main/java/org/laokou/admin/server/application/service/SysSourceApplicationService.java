package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.tenant.dto.SysSourceDTO;
import org.laokou.tenant.qo.SysSourceQo;
import org.laokou.tenant.vo.SysSourceVO;

/**
 * @author laokou
 */
public interface SysSourceApplicationService {

    /**
     * 查询多租户数据源分页
     * @param qo
     * @return
     */
    IPage<SysSourceVO> querySourcePage(SysSourceQo qo);

    /**
     * 新增多租户数据源
     * @param dto
     * @return
     */
    Boolean insertSource(SysSourceDTO dto);

    /**
     * 修改多租户数据源
     * @param dto
     * @return
     */
    Boolean updateSource(SysSourceDTO dto);

    /**
     * 删除多租户数据源
     * @param id
     * @return
     */
    Boolean deleteSource(Long id);

    /**
     * 数据源详情
     * @param id
     * @return
     */
    SysSourceVO getSourceById(Long id);
}
