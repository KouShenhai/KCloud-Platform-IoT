module laokou.common.netty {
    exports org.laokou.common.netty.config;
    requires lombok;
    requires io.netty.transport;
    requires spring.boot.autoconfigure;
    requires io.netty.common;
}