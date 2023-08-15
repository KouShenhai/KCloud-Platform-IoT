module laokou.common.shardingsphere {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires shardingsphere.jdbc.core;
    requires spring.cloud.starter.alibaba.nacos.config;
    requires laokou.common.core;
    requires laokou.common.i18n;
    requires nacos.client;
    requires com.google.common;
    requires spring.core;
}