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

package org.laokou.tenant.processor;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import org.aopalliance.intercept.MethodInvocation;
import org.laokou.auth.client.utils.UserUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.tenant.utils.DataBaseUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
public class DsTenantProcessor extends DsProcessor {

    public static final String TENANT = "#tenant";

    @Override
    public boolean matches(String key) {
        return key.startsWith(TENANT);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        DataBaseUtil dataBaseUtil = SpringContextUtil.getBean(DataBaseUtil.class);
        return dataBaseUtil.loadDataBase(UserUtil.getSourceName());
    }
}
