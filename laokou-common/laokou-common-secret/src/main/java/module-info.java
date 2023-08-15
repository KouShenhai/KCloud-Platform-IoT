module laokou.common.secret {
    exports org.laokou.common.secret.annotation;
    requires jakarta.servlet;
    requires org.aspectj.weaver;
    requires lombok;
    requires spring.context;
    requires spring.core;
    requires laokou.common.core;
    requires spring.boot.autoconfigure;
    requires laokou.common.i18n;
}