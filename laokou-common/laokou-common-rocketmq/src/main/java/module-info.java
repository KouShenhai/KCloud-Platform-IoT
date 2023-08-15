module laokou.common.rocketmq {
    exports org.laokou.common.rocketmq.template;
    exports org.laokou.common.rocketmq.dto;
    exports org.laokou.common.rocketmq.constant;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires lombok;
    requires rocketmq.spring.boot;
    requires spring.messaging;
    requires spring.beans;
    requires rocketmq.client;
}