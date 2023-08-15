module laokou.common.i18n {
    requires lombok;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.validation;
    requires spring.context;
    requires spring.core;
    exports org.laokou.common.i18n.dto;
    exports org.laokou.common.i18n.common;
    exports org.laokou.common.i18n.utils;
}