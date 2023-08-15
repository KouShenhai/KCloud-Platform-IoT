module laokou.common.mail {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires spring.beans;
    requires freemarker;
    requires laokou.common.redis;
    requires laokou.common.i18n;
    requires laokou.common.core;
    requires org.apache.commons.lang3;
    requires spring.context.support;
}