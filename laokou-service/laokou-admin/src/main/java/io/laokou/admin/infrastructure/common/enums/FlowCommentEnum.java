package io.laokou.admin.infrastructure.common.enums;

public enum  FlowCommentEnum {

    NORMAL("1","正常");

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
