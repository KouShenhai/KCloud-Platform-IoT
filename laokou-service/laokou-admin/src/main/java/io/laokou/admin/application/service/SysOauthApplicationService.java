package io.laokou.admin.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.laokou.admin.interfaces.dto.SysOauthDTO;
import io.laokou.admin.interfaces.qo.SysOauthQO;
import io.laokou.admin.interfaces.vo.SysOauthVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/11 0011 上午 9:42
 */
public interface SysOauthApplicationService {

    IPage<SysOauthVO> queryOauthPage(SysOauthQO qo);

    Boolean insertOauth(SysOauthDTO dto, HttpServletRequest request);

    Boolean updateOauth(SysOauthDTO dto, HttpServletRequest request);

    Boolean deleteOauth(Long id);

    SysOauthVO getOauthById(Long id);
}
