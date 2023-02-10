package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysOssDO;
import org.laokou.admin.server.interfaces.qo.SysOssQo;
import org.laokou.oss.client.vo.SysOssVO;

/**
 * @author laokou
 */
public interface SysOssService extends IService<SysOssDO> {

    /**
     * 删除oss
     * @param id
     * @return
     */
    Boolean deleteOss(Long id);

    /**
     * 获取版本号
     * @param id
     * @return
     */
    Integer getVersion(Long id);

    /**
     * 分页查询
     * @param page
     * @param qo
     * @return
     */
    IPage<SysOssVO> queryOssPage(IPage<SysOssVO> page,SysOssQo qo);

    /**
     * 查看详情
     * @param id
     * @return
     */
    SysOssVO getOssById(Long id);
}
