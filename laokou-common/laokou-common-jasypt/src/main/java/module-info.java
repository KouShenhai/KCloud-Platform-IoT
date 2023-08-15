module laokou.common.jasypt {
    requires lombok;
    requires spring.context;
    requires org.aspectj.weaver;
    requires spring.core;
    requires laokou.common.i18n;
    requires spring.boot.autoconfigure;
    requires laokou.common.core;
    requires org.apache.httpcomponents.client5.httpclient5;
    exports org.laokou.common.jasypt.utils;
}