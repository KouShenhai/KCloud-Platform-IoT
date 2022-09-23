/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
package org.laokou.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception工具类
 *
 * @author
 */
public class ExceptionUtil {

    /**
     * 获取异常信息
     *
     * @param ex 异常
     * @return 返回异常信息
     */
    public static String getErrorStackTrace(Exception ex) {

        try (StringWriter sw = new StringWriter();
             PrintWriter pw = new PrintWriter(sw, true)) {
            ex.printStackTrace(pw);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
