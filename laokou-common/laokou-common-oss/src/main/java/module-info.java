module laokou.common.oss {
    exports org.laokou.common.oss.service;
    exports org.laokou.common.oss.entity;
    exports org.laokou.common.oss.vo;
    exports org.laokou.common.oss.qo;
    exports org.laokou.common.oss.support;
    requires spring.boot.autoconfigure;
    requires org.mybatis.spring;
    requires spring.context;
    requires io.swagger.v3.oas.annotations;
    requires laokou.common.mybatis.plus;
    requires com.baomidou.mybatis.plus.annotation;
    requires lombok;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.core;
    requires laokou.common.i18n;
    requires com.baomidou.mybatis.plus.extension;
    requires laokou.common.core;
    requires dynamic.datasource.spring;
    requires aws.java.sdk.s3;
    requires aws.java.sdk.core;
    requires laokou.common.log;
    requires laokou.common.redis;
    requires laokou.common.security;
}