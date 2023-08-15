module laokou.common.sms {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires lombok;
    requires laokou.common.redis;
    requires laokou.common.i18n;
    requires freemarker;
    requires laokou.common.core;
    requires org.apache.commons.lang3;
    requires com.fasterxml.jackson.databind;
    requires spring.beans;
}