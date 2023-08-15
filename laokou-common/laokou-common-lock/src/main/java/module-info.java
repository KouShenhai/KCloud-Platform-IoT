module laokou.common.lock {
    exports org.laokou.common.lock.annotation;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires org.aspectj.weaver;
    requires spring.core;
    requires laokou.common.i18n;
    requires laokou.common.redis;
    requires redisson;
}