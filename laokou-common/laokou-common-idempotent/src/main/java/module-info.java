module laokou.common.idempotent {
    exports org.laokou.common.idempotent.annotation;
    requires lombok;
    requires laokou.common.redis;
    requires spring.data.redis;
    requires spring.core;
    requires org.aspectj.weaver;
    requires spring.context;
    requires laokou.common.i18n;
    requires laokou.common.core;
    requires jakarta.servlet;
    requires spring.boot.autoconfigure;
}