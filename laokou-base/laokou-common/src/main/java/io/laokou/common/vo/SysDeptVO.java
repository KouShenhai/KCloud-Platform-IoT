package io.laokou.common.vo;

import io.laokou.common.utils.TreeUtil;
import lombok.Data;

@Data
public class SysDeptVO extends TreeUtil.TreeNo<SysDeptVO> {

    private Integer status;

    private Integer sort;

}
