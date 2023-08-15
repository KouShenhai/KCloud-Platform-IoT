module laokou.common.log {
    requires org.aspectj.weaver;
    requires lombok;
    requires spring.core;
    requires spring.context;
    requires jakarta.servlet;
    requires laokou.common.core;
    requires laokou.common.security;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires laokou.common.ip.region;
    requires spring.web;
    requires org.mybatis.spring;
    requires spring.boot.autoconfigure;
    requires com.baomidou.mybatis.plus.annotation;
    requires laokou.common.mybatis.plus;
    requires io.swagger.v3.oas.annotations;
    requires com.fasterxml.jackson.databind;
    requires easyexcel.core;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.core;
    requires laokou.common.i18n;
    requires spring.tx;
    requires com.baomidou.mybatis.plus.extension;
    requires dynamic.datasource.spring;
    requires laokou.common.easy.excel;
}