package io.laokou.admin.infrastructure.common.enums;

/**
 * @author Kou Shenhai
 */

public enum  FlowCommentEnum {

    NORMAL("1","正常"),
    DELEGATE("4","委派");

    private final String type;

    private final String remark;

    FlowCommentEnum(String type,String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }
}
