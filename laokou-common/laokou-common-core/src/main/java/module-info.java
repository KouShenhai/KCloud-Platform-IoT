module laokou.common.core {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.webmvc;
    requires spring.web;
    requires laokou.common.i18n;
    requires micrometer.commons;
    requires jakarta.servlet;
    requires org.apache.commons.lang3;
    requires spring.core;
    requires lombok;
    requires spring.beans;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.commons.collections4;
    requires org.yaml.snakeyaml;
    requires spring.boot;
    requires spring.expression;
    requires freemarker;
    requires spring.context.support;
    exports org.laokou.common.core.constant;
    exports org.laokou.common.core.utils;
    exports org.laokou.common.core.enums;
    exports org.laokou.common.core.holder;
    exports org.laokou.common.core.vo;
}