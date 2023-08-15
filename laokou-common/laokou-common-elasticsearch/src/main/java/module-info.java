module laokou.common.elasticsearch {
    exports org.laokou.common.elasticsearch.annotation;
    exports org.laokou.common.elasticsearch.enums;
    exports org.laokou.common.elasticsearch.constant;
    exports org.laokou.common.elasticsearch.template;
    exports org.laokou.common.elasticsearch.vo;
    exports org.laokou.common.elasticsearch.qo;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires lombok;
    requires spring.beans;
    requires elasticsearch.rest.client;
    requires org.apache.httpcomponents.httpcore;
    requires laokou.common.i18n;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpasyncclient;
    requires org.apache.httpcomponents.httpcore.nio;
    requires elasticsearch.java;
    requires elasticsearch.rest.high.level.client;
    requires jakarta.validation;
    requires elasticsearch;
    requires lucene.queryparser;
    requires laokou.common.core;
    requires elasticsearch.x.content;
}