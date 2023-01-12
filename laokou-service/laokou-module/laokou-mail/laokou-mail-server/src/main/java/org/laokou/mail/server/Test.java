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

package org.laokou.mail.server;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author laokou
 */
public class Test {
    public static void main(String[] args) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        sender.setHost("smtp.qq.com");
//        sender.setUsername("2413176044@qq.com");
//        sender.setPassword("hhqkeodvfywfebaf");
//        SimpleMailMessage mail = new SimpleMailMessage();
//        mail.setFrom("2413176044@qq.com");
//        mail.setTo("2413176044@qq.com");
//        mail.setText("33333");
        sender.setHost("smtp.qq.com");
        sender.setUsername("2413176044@qq.com");
        sender.setPassword("hhqkeodvfywfebaf");
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("2413176044@qq.com");
        mail.setTo("m18974432576@163.com");
        mail.setText("33333");
        sender.send(mail);
    }
}
