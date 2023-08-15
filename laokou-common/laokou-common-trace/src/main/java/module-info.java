module laokou.common.trace {
    exports org.laokou.common.trace.annotation;
    requires org.aspectj.weaver;
    requires laokou.common.i18n;
    requires org.slf4j;
    requires lombok;
    requires spring.context;
    requires laokou.common.core;
    requires spring.webmvc;
    requires spring.boot.autoconfigure;
    requires jakarta.servlet;
    requires micrometer.commons;
}