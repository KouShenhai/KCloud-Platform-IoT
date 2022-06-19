package io.laokou.admin.application.service;
import com.alipay.api.AlipayApiException;
import io.laokou.common.user.UserDetail;
import io.laokou.admin.interfaces.vo.LoginVO;
import io.laokou.admin.interfaces.vo.UserInfoVO;
import io.laokou.admin.interfaces.dto.LoginDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * shiro接口
 * @author Kou Shenhai
 */
public interface SysAuthApplicationService {

    /**
     * 登录
     * @param loginDTO
     * @return
     * @throws Exception
     */
    LoginVO login(LoginDTO loginDTO) throws Exception;

    /**
     * 退出
     * @param userId
     * @return
     */
    Boolean logout(Long userId);

    /**
     * 生成验证码
     * @param uuid
     * @param response
     * @throws IOException
     */
    void captcha(String uuid, HttpServletResponse response) throws IOException;

    /**
     * 访问资源权限
     * @param Authorization
     * @param uri
     * @param method
     * @return
     */
    UserDetail resource(String Authorization, String uri, String method);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserInfoVO userInfo(Long userId);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserDetail getUserDetail(Long userId);

    /**
     * 支付宝登录
     * @param request
     * @param response
     * @throws AlipayApiException
     * @throws IOException
     */
    void zfbLogin(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException;

}
