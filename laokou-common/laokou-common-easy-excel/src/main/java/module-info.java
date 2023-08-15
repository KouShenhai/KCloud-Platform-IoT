module laokou.common.easy.excel {
    exports org.laokou.common.easy.excel.suppert;
    exports org.laokou.common.easy.excel.service;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires org.mybatis;
    requires jakarta.servlet;
    requires laokou.common.core;
    requires easyexcel.core;
    requires lombok;
}