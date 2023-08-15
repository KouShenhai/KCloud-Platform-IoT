module laokou.common.hbase {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires hbase.shaded.client;
    requires lombok;
    requires laokou.common.core;
}