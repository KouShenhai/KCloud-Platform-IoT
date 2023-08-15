module laokou.logstash.cllient {
    exports org.laokou.logstash.client.index;
    requires laokou.common.elasticsearch;
    requires com.fasterxml.jackson.annotation;
    requires lombok;
    requires spring.context;
}