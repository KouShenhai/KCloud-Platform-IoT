package io.laokou.common.enums;

/**
 * 超级管理员枚举
 * @author  Kou Shenhai
 */

public enum SuperAdminEnum {

    YES(1,"是"),
    NO(0,"否");

    private Integer value;
    private String desc;

    SuperAdminEnum(Integer value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public Integer value(){
        return this.value;
    }

    public static String getDesc(Integer value) {
        SuperAdminEnum[] enums = SuperAdminEnum.values();
        for (SuperAdminEnum superAdminEnum : enums) {
            if (superAdminEnum.value().equals(value)) {
                return superAdminEnum.desc();
            }
        }
        return null;
    }

    public String desc() {
        return desc;
    }
}
