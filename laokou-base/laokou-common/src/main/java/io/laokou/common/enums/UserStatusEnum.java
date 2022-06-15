package io.laokou.common.enums;

import lombok.AllArgsConstructor;

/**
 * 用户状态
 * @author Kou Shenhai
 */
@AllArgsConstructor
public enum UserStatusEnum {
    DISABLE(1),
    ENABLED(0);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
