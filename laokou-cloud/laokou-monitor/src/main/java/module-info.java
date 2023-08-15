module laokou.monitor {
    requires micrometer.commons;
    requires lombok;
    requires spring.context;
    requires de.codecentric.spring.boot.admin.server;
    requires reactor.core;
    requires spring.web;
    requires reactor.netty.http;
    requires io.netty.handler;
    requires spring.boot.autoconfigure;
    requires spring.security.web;
    requires spring.security.config;
    requires reactor.netty.core;
    requires jasypt.spring.boot;
    requires spring.cloud.commons;
    requires nacos.client;
    requires spring.boot;
}