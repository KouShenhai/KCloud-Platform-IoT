module laokou.common.seata {
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires seata.all;
    requires laokou.common.i18n;
}