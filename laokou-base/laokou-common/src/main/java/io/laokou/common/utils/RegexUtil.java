package io.laokou.common.utils;

import java.util.regex.Pattern;

/**
 * @author Kou Shenhai
 */
public class RegexUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    private static final String MOBILE_REGEX = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    private static final String SCORE_REGEX = "^(([0-9]+.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*.[0-9]+)|([0-9]*[1-9][0-9]*))$";

    private static final String GRADE_REGEX = "^[0-9]\\d*$";

    /**
     * 邮箱验证
     * @param email
     * @return
     */
    public static boolean emailRegex(String email) {
        return Pattern.matches(EMAIL_REGEX,email);
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

}
