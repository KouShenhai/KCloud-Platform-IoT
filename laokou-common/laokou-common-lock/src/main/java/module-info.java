module laokou.common.lock {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires org.aspectj.weaver;
    requires spring.core;
    requires laokou.common.i18n;
    requires laokou.common.redis;
    requires redisson;
}