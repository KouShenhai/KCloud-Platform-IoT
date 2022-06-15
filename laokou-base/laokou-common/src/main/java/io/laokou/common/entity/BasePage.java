package io.laokou.common.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BasePage {

    @NotNull(message = "请填写显示页数")
    private Integer pageNum;

    @NotNull(message = "请填写显示条数")
    private Integer pageSize;

}
