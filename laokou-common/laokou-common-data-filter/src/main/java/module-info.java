module laokou.common.data.filter {
    exports org.laokou.common.data.filter.annotation;
    requires spring.context;
    requires org.aspectj.weaver;
    requires lombok;
    requires laokou.common.i18n;
    requires laokou.auth.domain;
    requires laokou.common.security;
    requires laokou.common.core;
    requires spring.core;
    requires spring.boot.autoconfigure;
}