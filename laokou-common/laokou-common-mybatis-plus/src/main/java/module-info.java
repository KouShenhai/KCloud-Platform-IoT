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
    exports org.laokou.common.mybatisplus.database.dataobject;
}