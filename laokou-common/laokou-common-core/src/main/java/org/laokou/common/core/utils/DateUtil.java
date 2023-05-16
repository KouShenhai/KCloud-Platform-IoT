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
     * yyyyMM
     */
    public static final int YYYYMM = 2;

    /**
     * 时间格式
     */
    private static final String[] TIME_PATTERNS = {
              "yyyy-MM-dd HH:mm:ss"
            , "yyyyMMddHHmmss"
            , "yyyyMM"
    };

    public static String getTimePattern(int index) {
        if (index >= TIME_PATTERNS.length || index < 0) {
            throw new CustomException("时间格式不存在，请重新输入");
        }
        return TIME_PATTERNS[index];
    }

    public static String format(LocalDateTime localDateTime,int index) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(index);
        return localDateTime.format(dateTimeFormatter);
    }

    public static DateTimeFormatter getDateTimeFormatter(int index) {
        String timePattern = getTimePattern(index);
        return DateTimeFormatter.ofPattern(timePattern);
    }

    /**
     * 转换
     * @param dateTime 时间
     * @param index 索引
     * @return LocalDateTime
     */
    public static LocalDateTime parse(String dateTime,int index) {
        String timePattern = getTimePattern(index);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);
        return LocalDateTime.parse(dateTime,dateTimeFormatter);
    }

    /**
     * 获取 前/后 x天 的时间
     * @param localDateTime 时间
     * @param day 天
     * @return LocalDateTime
     */
    public static LocalDateTime plusDays(LocalDateTime localDateTime,int day) {
        return localDateTime.plusDays(day);
    }

    /**
     * 获取 前/后 x月 的时间
     * @param localDateTime 时间
     * @param month 月
     * @return LocalDateTime
     */
    public static LocalDateTime plusMonths(LocalDateTime localDateTime,int month) {
        return localDateTime.plusMonths(month);
    }

    /**
     * 获取 前/后 x年 的时间
     * @param localDateTime 时间
     * @param year 年
     * @return LocalDateTime
     */
    public static LocalDateTime plusYears(LocalDateTime localDateTime,int year) {
        return localDateTime.plusYears(year);
    }

    public static Date now() {
        return new Date();
    }

    public static void main(String[] args) {
        System.out.println(format(LocalDateTime.now(), YYYYMMDDHHMMSS));
        System.out.println(parse("20230311001155",YYYYMMDDHHMMSS));
    }

}
