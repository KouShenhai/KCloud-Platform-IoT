package io.laokou.admin.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.laokou.admin.domain.sys.entity.SysOauthDO;
import io.laokou.admin.interfaces.qo.SysOauthQO;
import io.laokou.admin.interfaces.vo.SysOauthVO;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:49
 */
public interface SysOauthService extends IService<SysOauthDO> {

    void deleteOauth(Long id);
    SysOauthVO getOauthById(Long id);
    IPage<SysOauthVO> getOauthList(IPage<SysOauthVO> page, SysOauthQO qo);

}
