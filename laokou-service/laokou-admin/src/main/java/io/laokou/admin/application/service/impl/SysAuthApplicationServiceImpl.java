package io.laokou.admin.application.service.impl;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import io.laokou.admin.domain.sys.repository.service.SysCaptchaService;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.domain.sys.entity.ZfbUserDO;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.admin.domain.sys.repository.service.ZfbUserService;
import io.laokou.admin.infrastructure.common.password.PasswordUtil;
import io.laokou.admin.infrastructure.common.password.TokenUtil;
import io.laokou.admin.interfaces.dto.LoginDTO;
import io.laokou.admin.interfaces.vo.LoginVO;
import io.laokou.common.constant.Constant;
import io.laokou.admin.infrastructure.common.enums.AuthTypeEnum;
import io.laokou.common.enums.ResultStatusEnum;
import io.laokou.admin.infrastructure.common.enums.SuperAdminEnum;
import io.laokou.admin.infrastructure.common.enums.UserStatusEnum;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.admin.infrastructure.common.password.RsaCoder;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.*;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.admin.interfaces.vo.UserInfoVO;
import io.laokou.log.publish.PublishFactory;
import io.laokou.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import javax.crypto.BadPaddingException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
/**
 * sso实现类
 * @author Kou Shenhai
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysAuthApplicationServiceImpl implements SysAuthApplicationService {

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Autowired
    private ZfbOauth zfbOauth;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public LoginVO login(LoginDTO loginDTO) throws Exception {
        //region Description
        //效验数据
        ValidatorUtil.validateEntity(loginDTO);
        String uuid = loginDTO.getUuid();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String captcha = loginDTO.getCaptcha();
        log.info("解密前，用户名：{}", username);
        log.info("解密前，密码：{}", password);
        //SRA私钥解密
        try {
            username = RsaCoder.decryptByPrivateKey(username);
            password = RsaCoder.decryptByPrivateKey(password);
        } catch (BadPaddingException e) {
            PublishFactory.recordLogin("未知用户", ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ACCOUNT_PASSWORD_ERROR));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        log.info("解密后，用户名：{}", username);
        log.info("解密后，密码：{}", password);
        //验证码是否正确
        boolean validate = sysCaptchaService.validate(uuid, captcha);
        if (!validate) {
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.CAPTCHA_ERROR));
            throw new CustomException(ErrorCode.CAPTCHA_ERROR);
        }
        return getLoginInfo(username,password,true);
        //endregion
    }

    private LoginVO getLoginInfo(String username,String password,boolean isUserPasswordFlag) throws IOException {
        //查询数据库
        UserDetail userDetail = getUserDetail(username);
        log.info("查询的数据：{}",userDetail);
        if (!isUserPasswordFlag && null == userDetail) {
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ACCOUNT_NOT_EXIST));
            throw new CustomException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        if (isUserPasswordFlag && null == userDetail){
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ACCOUNT_PASSWORD_ERROR));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if(isUserPasswordFlag && !PasswordUtil.matches(password, userDetail.getPassword())){
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ACCOUNT_PASSWORD_ERROR));
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (UserStatusEnum.DISABLE.ordinal() == userDetail.getStatus()) {
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ACCOUNT_DISABLE));
            throw new CustomException(ErrorCode.ACCOUNT_DISABLE);
        }
        if (CollectionUtils.isEmpty(getRoleIds(userDetail)) && SuperAdminEnum.NO.ordinal() == userDetail.getSuperAdmin()) {
            PublishFactory.recordLogin(username, ResultStatusEnum.FAIL.ordinal(),MessageUtil.getMessage(ErrorCode.ROLE_NOT_EXIST));
            throw new CustomException(ErrorCode.ROLE_NOT_EXIST);
        }
        PublishFactory.recordLogin(username, ResultStatusEnum.SUCCESS.ordinal(),"登录成功");
        //获取token
        String token = getToken(userDetail);
        return LoginVO.builder().token(token).build();
    }

    private String getToken(UserDetail userDetail) {
        //region Description
        //编号
        Long userId = userDetail.getId();
        //登录成功 > 生成token
        String token = TokenUtil.getToken(TokenUtil.getClaims(userId));
        log.info("Token is：{}", token);
        //资源列表放到redis中
        String userResourceKey = RedisKeyUtil.getUserResourceKey(userId);
        List<MenuVO> resourceList = sysMenuService.getMenuList(userDetail,true,1);
        List<String> permissionList = getPermissionList(userDetail);
        userDetail.setPermissionsList(permissionList);
        //用户信息
        String userInfoKey = RedisKeyUtil.getUserInfoKey(userId);
        redisUtil.delete(userInfoKey);
        redisUtil.delete(userResourceKey);
        redisUtil.set(userInfoKey,JSON.toJSONString(userDetail));
        redisUtil.set(userResourceKey, JSON.toJSONString(resourceList));
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        request.setAttribute(Constant.AUTHORIZATION_HEADER, token);
        return token;
        //endregion
    }

    private List<String> getPermissionList(UserDetail userDetail) {
        //region Description
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin()){
            return sysMenuService.getPermissionsList();
        }else{
            return sysMenuService.getPermissionsListByUserId(userDetail.getId());
        }
        //endregion
    }

    @Override
    public Boolean logout(Long userId) {
        //region Description
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        //删除相关信息
        removeInfo(request,userId);
        return true;
        //endregion
    }

    private void removeInfo(HttpServletRequest request,Long userId) {
        //region Description
        //删除缓存
        String userResourceKey = RedisKeyUtil.getUserResourceKey(userId);
        String userInfoKey = RedisKeyUtil.getUserInfoKey(userId);
        redisUtil.delete(userResourceKey);
        redisUtil.delete(userInfoKey);
        //退出
        request.removeAttribute(Constant.AUTHORIZATION_HEADER);
        //endregion
    }

    @Override
    public void captcha(String uuid, HttpServletResponse response) throws IOException {
        //region Description
        //生成图片验证码
        if (StringUtils.isBlank(uuid)) {
            throw new CustomException(ErrorCode.IDENTIFIER_NOT_NULL);
        }
        BufferedImage image = sysCaptchaService.createImage(uuid);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image,"jpg",out);
        out.close();
        //endregion
    }

    @Override
    public UserDetail resource(String Authorization, String uri, String method) {
        //region Description
        //1.获取用户信息
        Long userId = getUserId(Authorization);
        UserDetail userDetail = getUserDetail(userId);
        //2.获取所有资源列表
        List<MenuVO> resourceList = sysMenuService.getMenuList(null,null);
        //3.判断资源是否在资源列表列表里
        MenuVO resource = pathMatcher(uri, method, resourceList);
        //4.无需认证
        if (resource != null && AuthTypeEnum.NO_AUTH.ordinal() == resource.getAuthLevel()) {
            return userDetail;
        }
        //5.登录认证
        if (resource != null && AuthTypeEnum.LOGIN_AUTH.ordinal() == resource.getAuthLevel()) {
            return userDetail;
        }
        //6.不在资源列表，只要登录了，就能访问
        if (resource == null) {
            return userDetail;
        }
        //7.当前登录用户为超级管理员
        if (SuperAdminEnum.YES.ordinal() == userDetail.getSuperAdmin()) {
            return userDetail;
        }
        //8. 需要鉴权，获取用户资源列表
        resourceList = sysMenuService.getMenuList(userDetail,false,1);
        //9.如果不在用户资源列表里，则无权访问
        resource = pathMatcher(uri, method, resourceList);
        if (resource != null) {
            return userDetail;
        }
        throw new CustomException(ErrorCode.FORBIDDEN);
        //endregion
    }

    @Override
    public UserInfoVO userInfo(Long userId) {
        UserDetail userDetail = getUserDetail(userId);
        return UserInfoVO.builder().imgUrl(userDetail.getImgUrl())
                        .username(userDetail.getUsername())
                        .userId(userDetail.getId())
                        .mobile(userDetail.getMobile())
                        .email(userDetail.getEmail())
                        .roles(sysRoleService.getRoleListByUserId(userId))
                        .permissionList(userDetail.getPermissionsList()).build();
    }

    private MenuVO pathMatcher(String url, String method, List<MenuVO> resourceList) {
        //region Description
        for (MenuVO resource : resourceList) {
            if (StringUtils.isNotEmpty(url) && antPathMatcher.match(resource.getUrl(), url)
                    && method.equalsIgnoreCase(resource.getMethod())) {
                log.info("匹配成功");
                return resource;
            }
        }
        return null;
        //endregion
    }

    private Long getUserId(String Authorization) {
        //region Description
        if (TokenUtil.isExpiration(Authorization)) {
            throw new CustomException(ErrorCode.AUTHORIZATION_INVALID);
        }
        return TokenUtil.getUserId(Authorization);
        //endregion
    }

    @Override
    public UserDetail getUserDetail(Long userId) {
        //region Description
        String userInfoKey = RedisKeyUtil.getUserInfoKey(userId);
        String json = redisUtil.get(userInfoKey);
        UserDetail userDetail;
        if (StringUtils.isNotBlank(json)) {
            userDetail = JSON.parseObject(json, UserDetail.class);
        } else {
            userDetail = sysUserService.getUserDetail(userId,null);
            if (Objects.isNull(userDetail)) {
                throw new CustomException(ErrorCode.ACCOUNT_NOT_EXIST);
            }
            userDetail.setPermissionsList(getPermissionList(userDetail));
            redisUtil.set(userInfoKey, JSON.toJSONString(userDetail));
        }
        return userDetail;
        //endregion
    }

    @Override
    public void zfbLogin(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        zfbOauth.sendRedirectLogin(request,response);
    }

    private UserDetail getUserDetail(String username) {
        return sysUserService.getUserDetail(null, username);
    }

    private List<Long> getRoleIds(UserDetail userDetail) {
        Integer superAdmin = userDetail.getSuperAdmin();
        Long userId = userDetail.getId();
        if (SuperAdminEnum.YES.ordinal() == superAdmin) {
            return sysRoleService.getRoleIds();
        } else {
            return sysRoleService.getRoleIdsByUserId(userId);
        }
    }

    @Component
    public class ZfbOauth{
        @Autowired
        private ZfbUserService zfbUserService;
        @Value("${oauth.zfb.app_id}")
        private String APP_ID;
        @Value("${oauth.zfb.merchant_private_key}")
        private String MERCHANT_PRIVATE_KEY;
        @Value("${oauth.zfb.public_key}")
        private String PUBLIC_KEY;
        @Value("${oauth.zfb.sign_type}")
        private String SIGN_TYPE;
        @Value("${oauth.zfb.charset}")
        private String CHARSET;
        @Value("${oauth.zfb.gateway_url}")
        private String GATEWAY_URL;
        @Value("${oauth.zfb.encrypt_type}")
        private String ENCRYPT_TYPE;
        @Value("${oauth.zfb.redirect_url}")
        private String REDIRECT_URL;

        public void sendRedirectLogin(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
            final String authCode = request.getParameter("auth_code");
            log.info("appId:{}",APP_ID);
            log.info("authCode:{}",authCode);
            //初始化AliPayClient
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,APP_ID,MERCHANT_PRIVATE_KEY,SIGN_TYPE,CHARSET,PUBLIC_KEY,ENCRYPT_TYPE);
            // 通过authCode获取accessToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType("authorization_code");
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
            String accessToken = oauthTokenResponse.getAccessToken();
            log.info("accessToken:{}",accessToken);
            if (StringUtils.isNotBlank(accessToken)) {
                //根据accessToken获取用户信息
                final AlipayUserInfoShareResponse userInfoResponse = alipayClient.execute(new AlipayUserInfoShareRequest(), accessToken);
                log.info("userInfo:{}",JSON.toJSONString(userInfoResponse));
                if (userInfoResponse.isSuccess()) {
                    final String userId = userInfoResponse.getUserId();
                    final String city = userInfoResponse.getCity();
                    final String province = userInfoResponse.getProvince();
                    final String gender = userInfoResponse.getGender();
                    final String avatar = userInfoResponse.getAvatar();
                    ZfbUserDO entity = new ZfbUserDO();
                    entity.setId(userId);
                    entity.setAvatar(avatar);
                    entity.setProvince(province);
                    entity.setCity(city);
                    entity.setGender(gender);
                    zfbUserService.removeById(userId);
                    zfbUserService.save(entity);
                    sendRedirectLogin(sysUserService.getUsernameByOpenid(userId),response);
                }
            }
        }
        private void sendRedirectLogin(String username,HttpServletResponse response) throws IOException {
            String params = "";
            if (StringUtils.isNotBlank(username)) {
                final LoginVO loginInfo = getLoginInfo(username, null, false);
                if (loginInfo != null) {
                    params += "?" + Constant.ACCESS_TOKEN + "=" + loginInfo.getToken();
                }
            }
            response.sendRedirect(REDIRECT_URL + params);
        }
    }
}
