module laokou.common.data.cache {
    requires lombok;
    requires org.aspectj.weaver;
    requires spring.context;
    requires spring.core;
    requires com.github.benmanes.caffeine;
    requires laokou.common.core;
    requires laokou.common.redis;
    requires spring.data.redis;
    requires spring.boot.autoconfigure;
}