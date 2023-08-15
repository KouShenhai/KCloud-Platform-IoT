module laokou.common.mybatis.plus {
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires java.sql;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.core;
    requires lombok;
    requires spring.tx;
    requires org.mybatis;
    requires laokou.common.i18n;
    requires jsqlparser;
    requires laokou.common.core;
    requires com.baomidou.mybatis.plus.annotation;
    requires io.swagger.v3.oas.annotations;
    requires laokou.common.jasypt;
    requires com.google.common;
    requires dynamic.datasource.spring;
    exports org.laokou.common.mybatisplus.database.dataobject;
    exports org.laokou.common.mybatisplus.database;
    exports org.laokou.common.mybatisplus.utils;
    exports org.laokou.common.mybatisplus.handler;
    exports org.laokou.common.mybatisplus.context;
}