module laokou.auth.client {
    requires laokou.common.i18n;
    requires lombok;
    requires spring.context;
    exports org.laokou.auth.api;
    exports org.laokou.auth.dto;
    exports org.laokou.auth.dto.domainevent;
}