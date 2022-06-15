package io.laokou.common.utils;

import java.util.regex.Pattern;

/**
 * @author Kou Shenhai
 */
public class RegexUtil {

    private static final String EMAIL_REGEX = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+";

    private static final String MOBILE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

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



    public static void main(String[] args) {
        System.out.println(gradeRegex("0.8"));
        System.out.println(gradeRegex("0.0"));
        System.out.println(gradeRegex("10"));
        System.out.println(gradeRegex("11"));
        System.out.println(gradeRegex("-10"));
    }

}
