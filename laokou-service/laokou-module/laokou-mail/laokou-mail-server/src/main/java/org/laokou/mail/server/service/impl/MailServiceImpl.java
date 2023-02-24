/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.mail.server.service.impl;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.freemarker.utils.TemplateUtil;
import org.laokou.mail.server.service.MailService;
import org.laokou.redis.utils.RedisKeyUtil;
import org.laokou.redis.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
@RefreshScope
@Slf4j
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    private static final String CAPTCHA_TEMPLATE = "验证码：${captcha}，${minute}分钟内有效，请勿泄漏于他人！";
    private final RedisUtil redisUtil;
    
    @Override
    public Boolean sendMail(String toMail) throws TemplateException, IOException {
        // 验证邮箱
        boolean mailRegex = RegexUtil.mailRegex(toMail);
        if (!mailRegex) {
            throw new CustomException("邮箱格式不正确，请重新输入");
        }
        // 生成验证码
        String subject = "验证码";
        int minute = 5;
        String captcha = RandomStringUtils.randomNumeric(6);
        Map<String,Object> params = new HashMap<>(2);
        params.put("captcha",captcha);
        params.put("minute",minute);
        String content = TemplateUtil.getContent(CAPTCHA_TEMPLATE, params);
        // 发送邮件
        sendMail(subject,content,toMail);
        String userCaptchaKey = RedisKeyUtil.getUserCaptchaKey(toMail);
        redisUtil.set(userCaptchaKey,captcha,minute * 60);
        return true;
    }

    private void sendMail(String subject,String content,String toMail) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost(host);
            sender.setPassword(password);
            sender.setUsername(username);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(toMail);
            mailMessage.setSubject(subject);
            mailMessage.setFrom(username);
            mailMessage.setText(content);
            sender.send(mailMessage);
        } catch (Exception e) {
            log.info("错误信息：{}",e.getMessage());
        }
    }

}
