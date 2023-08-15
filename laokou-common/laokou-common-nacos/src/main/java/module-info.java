module laokou.common.nacos {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires nacos.client;
    requires lombok;
    requires spring.cloud.starter.alibaba.nacos.config;
    requires laokou.common.core;
    requires laokou.common.i18n;
    requires spring.cloud.commons;
    requires spring.cloud.starter.alibaba.nacos.discovery;
    exports org.laokou.common.nacos.utils;
    exports org.laokou.common.nacos.vo;
}