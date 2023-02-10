package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysOssDTO;
import org.laokou.admin.server.interfaces.qo.SysOssQo;
import org.laokou.oss.client.vo.SysOssVO;

/**
 * @author laokou
 */
public interface SysOssApplicationService {

    /**
     * 新增oss
     * @param dto
     * @return
     */
    Boolean insertOss(SysOssDTO dto);

    /**
     * 修改oss
     * @param dto
     * @return
     */
    Boolean updateOss(SysOssDTO dto);

    /**
     * 删除oss
     * @param id
     * @return
     */
    Boolean deleteOss(Long id);

    /**
     * 查询oss
     * @param qo
     * @return
     */
    IPage<SysOssVO> queryOssPage(SysOssQo qo);


    /**
     * 查询详情
     * @param id
     * @return
     */
    SysOssVO getOssById(Long id);

}
