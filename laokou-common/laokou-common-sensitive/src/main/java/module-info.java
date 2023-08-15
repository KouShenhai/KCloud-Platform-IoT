module laokou.common.sensitive {
    exports org.laokou.common.sensitive.enums;
    exports org.laokou.common.sensitive.utils;
    requires org.aspectj.weaver;
    requires laokou.common.i18n;
    requires spring.context;
    requires lombok;
    requires spring.boot.autoconfigure;
}