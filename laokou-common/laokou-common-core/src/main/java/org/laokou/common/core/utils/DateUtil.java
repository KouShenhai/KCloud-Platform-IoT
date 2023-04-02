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
package org.laokou.common.core.utils;
import org.laokou.common.i18n.core.CustomException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期处理
 * @author laokou
 */
public class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final int YYYY_MM_DD_HH_MM_SS = 0;
    /**
     * yyyyMMddHHmmss
     */
    public static final int YYYYMMDDHHMMSS = 1;

    /**
     * 时间格式
     */
    private static final String[] TIME_PATTERNS = {
            "yyyy-MM-dd HH:mm:ss"
          , "yyyyMMddHHmmss"
    };

    public static String getTimePattern(int index) {
        if (index >= TIME_PATTERNS.length || index < 0) {
            throw new CustomException("时间格式不存在，请重新输入");
        }
        return TIME_PATTERNS[index];
    }

    public static String format(LocalDateTime localDateTime,int index) {
        String timePattern = getTimePattern(index);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);
        return localDateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime parse(String format,int index) {
        String timePattern = getTimePattern(index);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);
        return LocalDateTime.parse(format,dateTimeFormatter);
    }

    public static Date now() {
        return new Date();
    }

    public static void main(String[] args) {
        System.out.println(format(LocalDateTime.now(), YYYYMMDDHHMMSS));
        System.out.println(parse("20230311001155",YYYYMMDDHHMMSS));
    }

}
