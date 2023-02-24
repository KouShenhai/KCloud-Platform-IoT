/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.auth.server.application.service.impl;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.laokou.auth.client.constant.AuthConstant;
import org.laokou.auth.client.user.UserDetail;
import org.laokou.auth.server.domain.sys.repository.service.SysCaptchaService;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.vo.OptionVO;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.auth.server.application.service.SysAuthApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.core.StatusCode;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.laokou.tenant.service.SysTenantService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.security.Principal;
import java.util.List;

/**
 * SpringSecurity最新版本更新
 * @author laokou
 */
@Service
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SysAuthApplicationServiceImpl implements SysAuthApplicationService {
    private final OAuth2AuthorizationService oAuth2AuthorizationService;
    private final RedisUtil redisUtil;
    private final SysCaptchaService sysCaptchaService;
    private final SysTenantService sysTenantService;

    @Override
    public String captcha(HttpServletRequest request) {
        // 判断唯一标识是否为空
        String uuid = request.getParameter(AuthConstant.UUID);
        log.info("唯一标识：{}",uuid);
        if (StringUtil.isEmpty(uuid)) {
            throw new CustomException(StatusCode.IDENTIFIER_NOT_NULL);
        }
        // 三个参数分别为宽、高、位数
        Captcha captcha = new GifCaptcha(130, 48, 4);
        // 设置字体，有默认字体，可以不用设置
        captcha.setFont(new Font("Verdana", Font.PLAIN, 32));
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        sysCaptchaService.setCode(uuid,captcha.text());
        return captcha.toBase64();
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        String token = request.getHeader(Constant.AUTHORIZATION_HEAD);
        if (StringUtil.isEmpty(token)) {
            return true;
        }
        token = token.substring(7);
        String accountKillKey = RedisKeyUtil.getAccountKillKey(token);
        if (redisUtil.hasKey(accountKillKey)) {
            redisUtil.delete(accountKillKey);
        }
        OAuth2Authorization oAuth2Authorization = oAuth2AuthorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (oAuth2Authorization == null) {
            return true;
        }
        UserDetail userDetail = (UserDetail) ((UsernamePasswordAuthenticationToken) oAuth2Authorization.getAttribute(Principal.class.getName())).getPrincipal();
        // 清空token
        oAuth2AuthorizationService.remove(oAuth2Authorization);
        // 用户key
        String userInfoKey = RedisKeyUtil.getUserInfoKey(token);
        if (redisUtil.hasKey(userInfoKey)) {
            redisUtil.delete(userInfoKey);
        }
        Long userId = userDetail.getUserId();
        // 菜单key
        String resourceTreeKey = RedisKeyUtil.getResourceTreeKey(userId);
        if (redisUtil.hasKey(resourceTreeKey)) {
            redisUtil.delete(resourceTreeKey);
        }
        // 消息key
        String messageUnReadKey = RedisKeyUtil.getMessageUnReadKey(userId);
        if (redisUtil.hasKey(messageUnReadKey)) {
            redisUtil.delete(messageUnReadKey);
        }
        return true;
    }

    @Override
    public List<OptionVO> getOptionList() {
        return sysTenantService.getOptionList();
    }

}
