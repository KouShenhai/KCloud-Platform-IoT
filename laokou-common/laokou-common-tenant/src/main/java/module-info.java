module laokou.common.tenant {
    exports org.laokou.common.tenant.service;
    exports org.laokou.common.tenant.entity;
    exports org.laokou.common.tenant.dto;
    exports org.laokou.common.tenant.vo;
    exports org.laokou.common.tenant.qo;
    requires spring.context;
    requires org.mybatis.spring;
    requires spring.boot.autoconfigure;
    requires jakarta.validation;
    requires lombok;
    requires io.swagger.v3.oas.annotations;
    requires laokou.common.mybatis.plus;
    requires com.baomidou.mybatis.plus.annotation;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.core;
    requires laokou.common.core;
    requires laokou.common.security;
    requires spring.aop;
    requires dynamic.datasource.spring;
    requires laokou.common.i18n;
    requires com.baomidou.mybatis.plus.extension;
    requires dynamic.datasource.creator;
    requires java.sql;
}