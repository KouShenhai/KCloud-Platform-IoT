module laokou.logstash.rocketmq {
    requires rocketmq.spring.boot;
    requires lombok;
    requires spring.context;
    requires rocketmq.common;
    requires laokou.common.elasticsearch;
    requires laokou.common.core;
    requires laokou.logstash.cllient;
    requires laokou.common.i18n;
    requires laokou.common.rocketmq;
    requires spring.cloud.commons;
    requires jasypt.spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.boot;
}