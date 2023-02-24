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
package org.laokou.auth.client.constant;
/**
 * @author laokou
 */
public interface AuthConstant {
   /**
    * 唯一标识
    */
   String UUID = "uuid";
   /**
    * 验证码
    */
   String CAPTCHA = "captcha";

   String TENANT_ID = "tenantId";

   /**
    * 登录成功
    */
   String LOGIN_SUCCESS_MSG = "登录成功";

   /**
    * 邮箱
    */
   String MAIL = "mail";

   /**
    * 手机
    */
   String MOBILE = "mobile";

   String DEFAULT_SOURCE = "master";

}
