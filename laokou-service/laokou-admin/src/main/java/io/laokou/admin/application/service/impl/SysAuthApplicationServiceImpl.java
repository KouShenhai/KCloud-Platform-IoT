package io.laokou.admin.application.service.impl;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import io.laokou.admin.application.service.CaptchaApplicationService;
import io.laokou.admin.application.service.SysAuthApplicationService;
import io.laokou.admin.domain.sys.entity.WxUserDO;
import io.laokou.admin.domain.sys.entity.WxgzhUserDO;
import io.laokou.admin.domain.sys.entity.ZfbUserDO;
import io.laokou.admin.domain.sys.repository.dao.WxUserDao;
import io.laokou.admin.domain.sys.repository.dao.WxgzhUserDao;
import io.laokou.admin.domain.sys.repository.dao.ZfbUserDao;
import io.laokou.admin.domain.sys.repository.service.SysRoleService;
import io.laokou.admin.domain.sys.repository.service.SysUserService;
import io.laokou.admin.domain.sys.repository.service.SysMenuService;
import io.laokou.admin.infrastructure.common.password.PasswordUtil;
import io.laokou.admin.infrastructure.common.password.TokenUtil;
import io.laokou.admin.interfaces.dto.LoginDTO;
import io.laokou.admin.interfaces.dto.CodeAuthDTO;
import io.laokou.admin.interfaces.vo.LoginVO;
import io.laokou.common.constant.Constant;
import io.laokou.common.enums.AuthTypeEnum;
import io.laokou.common.enums.SuperAdminEnum;
import io.laokou.common.enums.UserStatusEnum;
import io.laokou.common.exception.CustomException;
import io.laokou.common.exception.ErrorCode;
import io.laokou.admin.infrastructure.common.password.RsaCoder;
import io.laokou.common.user.UserDetail;
import io.laokou.common.utils.*;
import io.laokou.admin.interfaces.vo.MenuVO;
import io.laokou.admin.interfaces.vo.UserInfoVO;
import io.laokou.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import javax.crypto.BadPaddingException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * sso实现类
 * @author Kou Shenhai
 */
@Service
@Slf4j
public class SysAuthApplicationServiceImpl implements SysAuthApplicationService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CaptchaApplicationService captchaApplicationService;

    @Autowired
    private ZfbOauth zfbOauth;

    @Autowired
    private WxgzhOauth wxgzhOauth;

    @Autowired
    private WxOauth wxOauth;

    @Autowired
    private SysRoleService sysRoleService;

    private static final String LOGIN_URL = "https://1.com/im/login.html";

    @Override
    public LoginVO login(LoginDTO loginDTO) throws Exception {
        //region Description
        //效验数据
        ValidatorUtil.validateEntity(loginDTO);
        String uuid = loginDTO.getUuid();
        String captcha = loginDTO.getCaptcha();
        //验证码是否正确
        boolean validate = captchaApplicationService.validate(uuid, captcha);
        if (!validate) {
            throw new CustomException(ErrorCode.CAPTCHA_ERROR);
        }
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        log.info("解密前，用户名：{}", username);
        log.info("解密前，密码：{}", password);
        //SRA私钥解密
        try {
            username = RsaCoder.decryptByPrivateKey(username);
            password = RsaCoder.decryptByPrivateKey(password);
        } catch (BadPaddingException e) {
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        log.info("解密后，用户名：{}", username);
        log.info("解密后，密码：{}", password);
        return getLoginInfo(username,password,true);
        //endregion
    }

    @Override
    public LoginVO codeLogin(CodeAuthDTO codeAuthDTO) {
        return getLoginInfo(null,null,false);
    }

    private LoginVO getLoginInfo(String username,String password,boolean isUserPasswordFlag) {
        //查询数据库
        UserDetail userDetail = getUserDetail(username);
        log.info("查询的数据：{}",userDetail);
        if (!isUserPasswordFlag && null == userDetail) {
            throw new CustomException(ErrorCode.ACCOUNT_NOT_EXIST);
        }
        if (isUserPasswordFlag && null == userDetail){
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if(isUserPasswordFlag && !PasswordUtil.matches(password, userDetail.getPassword())){
            throw new CustomException(ErrorCode.ACCOUNT_PASSWORD_ERROR);
        }
        if (UserStatusEnum.DISABLE.getValue().equals(userDetail.getStatus())) {
            throw new CustomException(ErrorCode.ACCOUNT_DISABLE);
        }
        if (CollectionUtils.isEmpty(userDetail.getRoleIds()) && SuperAdminEnum.NO.value().equals(userDetail.getSuperAdmin())) {
            throw new CustomException(ErrorCode.ROLE_NOT_EXIST);
        }
        //获取Authorization
        String Authorization = getAuthorization(userDetail);
        return LoginVO.builder().token(Authorization).build();
    }

    private String getAuthorization(UserDetail userDetail) {
        //region Description
        //编号
        Long userId = userDetail.getId();
        //登录成功 > 生成token
        String Authorization = TokenUtil.getToken(TokenUtil.getClaims(userId));
        log.info("Authorization is：{}", Authorization);
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
        request.setAttribute(Constant.AUTHORIZATION_HEADER, Authorization);
        return Authorization;
        //endregion
    }

    private List<String> getPermissionList(UserDetail userDetail) {
        //region Description
        boolean isSuperAdmin = userDetail.getSuperAdmin().equals(SuperAdminEnum.YES.value());
        if (isSuperAdmin){
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
        return Boolean.TRUE;
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
        BufferedImage image = captchaApplicationService.createImage(uuid);
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
        if (resource != null && resource.getAuthLevel().equals(AuthTypeEnum.NO_AUTH.value())) {
            return userDetail;
        }
        //5.登录认证
        if (resource != null && resource.getAuthLevel().equals(AuthTypeEnum.LOGIN_AUTH.value())) {
            return userDetail;
        }
        //6.不在资源列表，只要登录了，就能访问
        if (resource == null) {
            return userDetail;
        }
        //7.当前登录用户为超级管理员
        if (userDetail.getSuperAdmin().equals(SuperAdminEnum.YES.value())) {
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
            userDetail.setRoleIds(getRoleIds(userDetail));
            userDetail.setPermissionsList(getPermissionList(userDetail));
            redisUtil.set(userInfoKey, JSON.toJSONString(userDetail));
        }
        return userDetail;
        //endregion
    }

    @Override
    public void zfbLogin(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        final String username = zfbOauth.getUsername(request);
        sendRedirectLogin(username,response);
    }

    private void sendRedirectLogin(String username,HttpServletResponse response) throws IOException {
        String params = "";
        if (StringUtils.isNotBlank(username)) {
            final LoginVO loginInfo = getLoginInfo(username, null, false);
            if (loginInfo != null) {
                params += "?" + Constant.AUTHORIZATION_HEADER + "=" + loginInfo.getToken();
            }
        }
        response.sendRedirect(LOGIN_URL + params);
    }

    @Override
    public String wxgzhSign(HttpServletRequest request) throws IOException {
       return wxgzhOauth.wxgzhSign(request);
    }

    @Override
    public LoginVO wxgzhLogin(HttpServletRequest request) {
        final String sceneId = request.getParameter("sceneId");
        final String wxOpenidKey = RedisKeyUtil.getWxOpenidKey(sceneId);
        final String openid = redisUtil.get(wxOpenidKey);
        if (StringUtils.isBlank(openid)) {
            throw new CustomException("用户未扫码，请稍后再试");
        }
        redisUtil.delete(wxOpenidKey);
        String username = sysUserService.getUsernameByOpenid("",openid,"");
        if (StringUtils.isBlank(username)) {
            throw new CustomException("微信账号未绑定，请绑定后再试");
        }
        return getLoginInfo(username,null,false);
    }

    @Override
    public JSONObject getWxgzhTicket() throws IOException {
        return wxgzhOauth.getWxgzhTicket();
    }

    @Override
    public void getWxQRCode(HttpServletResponse response) throws IOException {
        wxOauth.getWxQRCode(response);
    }

    @Override
    public void wxLogin(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String username = wxOauth.callbackInfo(request);
        sendRedirectLogin(username,response);
    }

    private UserDetail getUserDetail(String username) {
        UserDetail userDetail = sysUserService.getUserDetail(null, username);
        if (userDetail != null) {
            userDetail.setRoleIds(getRoleIds(userDetail));
        }
        return userDetail;
    }

    private List<Long> getRoleIds(UserDetail userDetail) {
        Integer superAdmin = userDetail.getSuperAdmin();
        Long userId = userDetail.getId();
        if (SuperAdminEnum.YES.value().equals(superAdmin)) {
            return sysRoleService.getRoleIds();
        } else {
            return sysRoleService.getRoleIdsByUserId(userId);
        }
    }

    @Component
    public class ZfbOauth{

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

        @Autowired
        private ZfbUserDao zfbUserDao;

        public String getUsername(HttpServletRequest request) throws AlipayApiException {
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
                    zfbUserDao.deleteById(userId);
                    zfbUserDao.insert(entity);
                    return sysUserService.getUsernameByOpenid(userId,"","");
                }
            }
            return null;
        }

    }

    @Component
    public class WxgzhOauth {

        @Value("${oauth.wxgzh.app_id}")
        private String APP_ID;
        @Value("${oauth.wxgzh.app_secret}")
        private String APP_SECRET;
        @Value("${oauth.wxgzh.access_token_url}")
        private String ACCESS_TOKEN_URL;
        @Value("${oauth.wxgzh.user_info_url}")
        private String USER_INFO_URL;
        @Value("${oauth.wxgzh.qr_code_url}")
        private String QR_CODE_URL;

        @Autowired
        private RedisUtil redisUtil;

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private WxgzhUserDao wxgzhUserDao;

        private final Long EXPIRE_DATE = 30 * 24 * 3600L;

        private String getAccessToken() throws IOException {
            String wxTokenKey = RedisKeyUtil.getWxTokenKey();
            String wxTokenJson = redisUtil.get(wxTokenKey);
            if (wxTokenJson == null) {
                String tokenUri = ACCESS_TOKEN_URL.replace("APPID", APP_ID).replace("APPSECRET", APP_SECRET);
                String resultJson = HttpUtil.doGet(tokenUri, new HashMap<>(0));
                if (StringUtils.isNotBlank(resultJson)) {
                    JSONObject jsonObject = JSONObject.parseObject(resultJson);
                    String accessToken = jsonObject.getString(Constant.ACCESS_TOKEN);
                    Long expireIn = jsonObject.getLong(Constant.EXPIRES_IN);
                    redisUtil.set(wxTokenKey,accessToken,expireIn);
                    return accessToken;
                }
            }
            return wxTokenJson;
        }

        public JSONObject getWxgzhTicket() throws IOException {
            String accessToken = getAccessToken();
            String qrCodeUrl = QR_CODE_URL.replace("TOKEN", accessToken);
            String sceneId = IdUtil.simpleUUID();
            String params = "{\"expire_seconds\": " + EXPIRE_DATE + ", \"action_name\": \"QR_STR_SCENE\"" +", \"action_info\": {\"scene\": {\"scene_str\": \"" + sceneId + "\"}}}";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> requestEntity = new HttpEntity<>(params, headers);
            String resultJson = restTemplate.postForEntity(qrCodeUrl, requestEntity, String.class).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultJson);
            jsonObject.put("sceneId",sceneId);
            return jsonObject;
        }

        private String callbackInfo(HttpServletRequest request) throws IOException {
            WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());
            log.info("message：{}",message);
            String eventKey = message.getEventKey();
            String type = message.getMsgType();
            String openid = message.getFromUser();
            String accessToken = getAccessToken();
            if ("event".equals(type)) {
                //获取用户信息
                String userInfoUrl = USER_INFO_URL.replace("OPENID",openid).replace("ACCESS_TOKEN",accessToken);
                log.info("userInfoUrl:{}",userInfoUrl);
                String resultJson = HttpUtil.doGet(userInfoUrl, new HashMap<>(0));
                final JSONObject jsonObject = JSONObject.parseObject(resultJson);
                String nickname = jsonObject.getString("nickname");
                Integer sex = jsonObject.getInteger("sex");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                String country = jsonObject.getString("country");
                String headimgurl = jsonObject.getString("headimgurl");
                final String remark = jsonObject.getString("remark");
                WxgzhUserDO entity = new WxgzhUserDO();
                entity.setId(openid);
                entity.setCity(city);
                entity.setCountry(country);
                entity.setProvince(province);
                entity.setRemark(remark);
                entity.setSex(sex);
                entity.setNickname(nickname);
                entity.setHeadimgurl(headimgurl);
                wxgzhUserDao.deleteById(openid);
                wxgzhUserDao.insert(entity);
                String wxOpenidKey = RedisKeyUtil.getWxOpenidKey(eventKey);
                redisUtil.set(wxOpenidKey,openid);
                return resultJson;
            }
            return null;
        }

        public String wxgzhSign(HttpServletRequest request) throws IOException {
           /*开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带参数如下表所示：
            参数	描述
            signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
            timestamp	时间戳
            nonce	随机数
            echostr	随机字符串*/
            final String signature = request.getParameter("signature");
            final String timestamp = request.getParameter("timestamp");
            final String nonce = request.getParameter("nonce");
            final String echostr = request.getParameter("echostr");
            log.info("获取相关request：{}", JSON.toJSONString(request.getParameterMap()));
            return WxgzhUtils.CheckSignature(signature,timestamp,nonce) && StringUtils.isNotBlank(echostr) ? echostr : callbackInfo(request);
        }
    }

    @Component
    /**
     *  简单来说 三个请求url的含义分辨为
     *  第一个：根据回调地址（就是用户扫描登录确以后微信平台会自己调用的接口）和 appid(微信密钥id) 请求二维码
     *  第二个：根据返回的code（临时票据，可理解是一个微信用户授权给你后，返回的一个code）、
     *  appid(微信密钥id)、appsecret（微信密钥secret） 获取 access_token（可以理解为接口调用凭证，有了这个才能去请求成功第三个url）、和openid（微信id）
     *  第三个：根据access_token和openid查询用户基本信息。
     */
    public class WxOauth {
        @Value("${oauth.wx.app_id}")
        private String APP_ID;
        @Value("${oauth.wx.app_secret}")
        private String APP_SECRET;
        @Value("${oauth.wx.redirect_url}")
        private String REDIRECT_URL;

        @Autowired
        private WxUserDao wxUserDao;

        private static final String STATE = "123";

        private static final String BASE_URI = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        private static final String BASE_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        private static final String BASE_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";

        public void getWxQRCode(HttpServletResponse response) throws IOException {
            String redirectUrl = URLEncoder.encode(REDIRECT_URL, StandardCharsets.UTF_8.name());
            response.sendRedirect(String.format(BASE_URI,APP_ID,redirectUrl,STATE));
        }

        public String callbackInfo(HttpServletRequest request) throws IOException {
            String code = request.getParameter("code");
            String resultToken = HttpUtil.doGet(String.format(BASE_TOKEN_URL, APP_ID, APP_SECRET, code), new HashMap<>(0));
            log.info("获取返回数据：{}",resultToken);
            JSONObject jsonObject = JSONObject.parseObject(resultToken);
            String accessToken = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");
            //获取用户信息
            String resultUserInfo = HttpUtil.doGet(String.format(BASE_USER_INFO_URL, accessToken, openid), new HashMap<>(0));
            log.info("获取返回数据：{}",resultUserInfo);
            JSONObject json = JSONObject.parseObject(resultUserInfo);
            String nickname = json.getString("nickname");
            Integer sex = json.getInteger("sex");
            String province = json.getString("province");
            String city = json.getString("city");
            String country = json.getString("country");
            String headimgurl = json.getString("headimgurl");
            WxUserDO entity = new WxUserDO();
            entity.setId(openid);
            entity.setCity(city);
            entity.setCountry(country);
            entity.setProvince(province);
            entity.setSex(sex);
            entity.setNickname(nickname);
            entity.setHeadimgurl(headimgurl);
            wxUserDao.deleteById(openid);
            wxUserDao.insert(entity);
            return sysUserService.getUsernameByOpenid("","",openid);
        }

    }
}
