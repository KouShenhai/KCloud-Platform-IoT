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

package org.laokou.tenant.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.core.utils.StringUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.tenant.service.SysSourceService;
import org.laokou.tenant.vo.SysSourceVO;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataBaseUtil {

    private final SysSourceService sysSourceService;

    public String loadDataBase(String sourceName) {
        if (StringUtil.isEmpty(sourceName)) {
            throw new CustomException("数据源名称不能为空");
        }
        if (!checkDataBase(sourceName)) {
            dynamicAddDataBase(sourceName);
        }
        return sourceName;
    }

    private void dynamicAddDataBase(String sourceName) {
        SysSourceVO sourceVO = sysSourceService.querySource(sourceName);
        DataSourceProperty properties = new DataSourceProperty ();
        properties.setUsername(sourceVO.getUsername());
        properties.setPassword(sourceVO.getPassword());
        properties.setUrl(sourceVO.getUrl());
        properties.setDriverClassName(sourceVO.getDriverClassName());
        // 验证数据源
        connectDataBase(properties);
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextUtil.getBean(DynamicRoutingDataSource.class);
        DefaultDataSourceCreator dataSourceCreator = SpringContextUtil.getBean(DefaultDataSourceCreator.class);
        DataSource dataSource = dataSourceCreator.createDataSource(properties);
        dynamicRoutingDataSource.addDataSource(sourceName,dataSource);
    }

    private boolean checkDataBase(String sourceName) {
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextUtil.getBean(DynamicRoutingDataSource.class);
        return dynamicRoutingDataSource.getDataSources().containsKey(sourceName);
    }

    private void connectDataBase(DataSourceProperty properties) {
        try {
            Class.forName(properties.getDriverClassName());
        } catch (Exception e) {
            log.error("数据源驱动加载失败，错误信息：{}",e.getMessage());
            throw new CustomException("数据源驱动加载失败，请检查相关配置");
        }
        try {
            Connection connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(), properties.getPassword());
            connection.close();
        } catch (Exception e) {
            log.error("数据源连接失败，错误信息：{}",e.getMessage());
            throw new CustomException("数据源连接失败，请检查相关配置");
        }
    }

}
