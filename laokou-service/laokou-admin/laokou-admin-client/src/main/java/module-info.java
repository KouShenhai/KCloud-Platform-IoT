module laokou.admin.client {
    requires laokou.common.i18n;
    requires lombok;
    requires com.github.oshi;
    requires laokou.common.core;
    requires java.management;
    requires jakarta.validation;
    exports org.laokou.admin.client.api;
    exports org.laokou.admin.client.dto;
    exports org.laokou.admin.client.dto.clientobject;
    exports org.laokou.admin.client.vo;
    exports org.laokou.admin.client.enums;
}