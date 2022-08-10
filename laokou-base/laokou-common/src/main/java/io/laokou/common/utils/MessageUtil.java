package io.laokou.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author 86189
 */
public class MessageUtil {
    private static MessageSource messageSource;
    static {
        messageSource = (MessageSource) SpringContextUtil.getBean("messageSource");
    }

    public static String getMessage(int code){
        return getMessage(code, new String[0]);
    }

    public static String getMessage(int code, String... params){
        return messageSource.getMessage(code + "", params, LocaleContextHolder.getLocale());
    }

}
