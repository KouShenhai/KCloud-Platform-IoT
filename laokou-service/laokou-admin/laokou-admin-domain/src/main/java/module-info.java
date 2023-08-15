module laokou.admin.domain {
    requires laokou.admin.client;
    requires laokou.common.i18n;
    requires lombok;
    exports org.laokou.admin.domain.gateway;
    exports org.laokou.admin.domain.user;
}