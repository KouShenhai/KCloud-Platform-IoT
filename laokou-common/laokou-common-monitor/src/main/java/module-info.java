module laokou.common.monitor {
    requires spring.boot.autoconfigure;
    requires de.codecentric.spring.boot.admin.client;
    requires spring.boot;
    requires spring.boot.actuator.autoconfigure;
    requires spring.context;
    requires jakarta.servlet;
    requires spring.boot.actuator;
    requires spring.beans;
    requires spring.web;
    requires laokou.common.core;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires spring.webflux;
    requires io.netty.handler;
    requires reactor.netty.http;
    requires reactor.netty.core;
    requires spring.core;
}