module laokou.admin.infrastructure {
    requires laokou.common.core;
    requires laokou.admin.client;
    requires laokou.admin.domain;
    requires io.swagger.v3.oas.annotations;
    requires com.baomidou.mybatis.plus.annotation;
    requires laokou.common.mybatis.plus;
    requires lombok;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.core;
    requires spring.context;
    requires spring.security.crypto;
    requires dynamic.datasource.spring;
    requires spring.tx;
    requires laokou.common.jasypt;
    requires laokou.common.i18n;
    requires laokou.common.data.filter;
    requires laokou.common.security;
}