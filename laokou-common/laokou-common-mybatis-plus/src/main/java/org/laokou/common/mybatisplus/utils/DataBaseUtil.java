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

package org.laokou.common.mybatisplus.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.swagger.exception.CustomException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author laokou
 */
@UtilityClass
@Slf4j
public class DataBaseUtil {

    public void connectDataBase(String driverClassName,String url,String username,String password) {
        try {
            Class.forName(driverClassName);
        } catch (Exception e) {
            log.error("数据源驱动加载失败，错误信息：{}",e.getMessage());
            throw new CustomException("数据源驱动加载失败，请检查相关配置");
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.close();
        } catch (Exception e) {
            log.error("数据源连接失败，错误信息：{}",e.getMessage());
            throw new CustomException("数据源连接失败，请检查相关配置");
        }
    }

}
