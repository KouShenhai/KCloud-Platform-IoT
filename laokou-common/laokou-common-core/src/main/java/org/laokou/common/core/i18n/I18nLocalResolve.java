package org.laokou.common.core.i18n;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.i18n.utils.LocaleUtil;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

import java.util.Locale;

/**
 * @author livk
 */
public class I18nLocalResolve extends AbstractLocaleContextResolver {
    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        Locale locale = LocaleUtil.toLocale(language);
        return new SimpleLocaleContext(locale);
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        throw new UnsupportedOperationException("Cannot change fixed locale - use a different locale resolution strategy");
    }
}
