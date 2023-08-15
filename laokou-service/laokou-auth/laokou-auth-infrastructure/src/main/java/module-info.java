module laokou.auth.infrastructure {
    requires spring.context;
    requires laokou.common.core;
    requires lombok;
    requires spring.web;
    requires spring.security.oauth2.core;
    requires spring.core;
    requires laokou.common.i18n;
    requires jakarta.servlet;
    requires com.baomidou.mybatis.plus.annotation;
    requires laokou.common.mybatis.plus;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.core;
    requires laokou.auth.domain;
    requires laokou.common.redis;
    requires UserAgentUtils;
    requires laokou.auth.client;
    requires laokou.common.ip.region;
    requires dynamic.datasource.spring;
    exports org.laokou.auth.common.exception.handler;
    exports org.laokou.auth.common.exception;
    exports org.laokou.auth.common;
    exports org.laokou.auth.gatewayimpl.database;
    exports org.laokou.auth.gatewayimpl.database.dataobject;
}