package io.laokou.admin.infrastructure.common.enums;
/**
 * @author Kou Shenhai
 */
public enum  FlowCommentEnum {
    NORMAL("1"),
    DELEGATE("4");
    private final String type;
    FlowCommentEnum(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
