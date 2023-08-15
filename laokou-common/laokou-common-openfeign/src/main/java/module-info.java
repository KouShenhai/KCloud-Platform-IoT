module laokou.common.openfeign {
    exports org.laokou.common.openfeign.constant;
    requires spring.context;
    requires feign.core;
    requires jakarta.servlet;
    requires laokou.common.core;
    requires spring.boot.autoconfigure;
    requires spring.cloud.openfeign.core;
}