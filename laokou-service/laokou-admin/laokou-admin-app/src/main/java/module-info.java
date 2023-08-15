module laokou.admin.app {
    requires lombok;
    requires laokou.common.redis;
    requires laokou.admin.client;
    requires laokou.common.i18n;
    requires laokou.auth.domain;
    requires laokou.common.jasypt;
    requires spring.context;
    requires laokou.common.security;
    requires laokou.admin.domain;
    requires laokou.common.core;
}