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
package org.laokou.rocketmq.consumer.feign.mail;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.rocketmq.consumer.feign.mail.fallback.MailApiFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author laokou
 */
@FeignClient(value = ServiceConstant.LAOKOU_MAIL,path = "/api", fallbackFactory = MailApiFeignClientFallback.class)
@Service
public interface MailApiFeignClient {

    /**
     * 发送邮件
     * @param mail
     * @return
     */
    @GetMapping("/send")
    HttpResult<Boolean> send(@RequestParam("mail")String mail);

}
