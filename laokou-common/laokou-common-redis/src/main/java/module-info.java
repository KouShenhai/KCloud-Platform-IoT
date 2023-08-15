module laokou.common.redis {
    requires spring.boot.autoconfigure;
    requires spring.data.redis;
    requires spring.context;
    requires reactor.core;
    requires spring.core;
    requires redisson;
    requires com.fasterxml.jackson.databind;
    requires io.netty.buffer;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires laokou.common.core;
    requires spring.boot;
    requires lombok;
    requires laokou.common.i18n;
    exports org.laokou.common.redis.utils;
}