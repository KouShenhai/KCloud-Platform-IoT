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
package org.laokou.auth.server.domain.sys.repository.service;

/**
 * @author laokou
 */
public interface SysCaptchaService {

    /**
     * 图片验证码
     * @param uuid 唯一标识
     * @param code
     * @return
     */
    Boolean setCode(String uuid,String code);

    /**
     * 验证码效验
     * @param uuid
     * @param code
     * @return
     */
    Boolean validate(String uuid,String code);
}
