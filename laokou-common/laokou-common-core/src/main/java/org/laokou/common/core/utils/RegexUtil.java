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

import java.util.regex.Pattern;

/**
 * @author laokou
 */
public class RegexUtil {

    private static final String MAIL_REGEX = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    private static final String MOBILE_REGEX = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    private static final String SCORE_REGEX = "^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$";

    private static final String GRADE_REGEX = "^[0-9]\\d*$";

    private static final String SOURCE_REGEX = "^[a-zA-Z]+_+([0-9]+)+$";

    /**
     * 邮箱验证
     * @param mail
     * @return
     */
    public static boolean mailRegex(String mail) {
        return Pattern.matches(MAIL_REGEX,mail);
    }

    public static boolean sourceRegex(String sourceName) {
        return Pattern.matches(SOURCE_REGEX, sourceName);
    }

    /**
     * 手机号验证
     * @param mobile
     * @return
     */
    public static boolean mobileRegex(String mobile) {
        return Pattern.matches(MOBILE_REGEX,mobile);
    }

    /**
     * 浮点数验证
     * @param score
     * @return
     */
    public static boolean scoreRegex(String score) {
        return Pattern.matches(SCORE_REGEX,score);
    }

    /**
     * 正整数验证
     * @param grade
     * @return
     */
    public static boolean gradeRegex(String grade) {
        return Pattern.matches(GRADE_REGEX,grade);
    }

    public static void main(String[] args) {
        System.out.println(mailRegex("24131760@qq"));
        boolean sourceRegex = sourceRegex("tenant_000001");
        System.out.println(sourceRegex);
    }

}
