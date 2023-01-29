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

package org.laokou.common.dynamic.datasource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}
 * @author laokou
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {

    /**
     * 存放数据源
     */
    private static final ThreadLocal<String> DATASOURCE_HOLDER = new ThreadLocal<>();

    /**
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String name) {
        DATASOURCE_HOLDER.set(name);
    }

    public static String getDataSource() {
        return DATASOURCE_HOLDER.get();
    }

    public static void clearDataSource() {
        DATASOURCE_HOLDER.remove();
    }

    @Override
    public void destroy() {

    }
}
